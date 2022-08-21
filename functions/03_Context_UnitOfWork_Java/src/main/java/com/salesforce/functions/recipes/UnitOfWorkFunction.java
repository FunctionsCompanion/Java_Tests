package com.salesforce.functions.recipes;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.DataApi;
import com.salesforce.functions.jvm.sdk.data.Record;
import com.salesforce.functions.jvm.sdk.data.RecordModificationResult;
import com.salesforce.functions.jvm.sdk.data.ReferenceId;
import com.salesforce.functions.jvm.sdk.data.builder.UnitOfWorkBuilder;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Map;
import logger.FCLogger;

/**
 * Receives a payload containing Account, Contact, and Case details and uses the Unit of Work
 * pattern to assign the corresponding values to to its Record while maintaining the relationships.
 * It then commits the unit of work and returns the Record Id's for each object.
 */
public class UnitOfWorkFunction implements SalesforceFunction<Object, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(UnitOfWorkFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<Object> event, Context context)
      throws Exception {

    FCLogger fc = new FCLogger(event, context, LOGGER);
    // FunctionInput fiObject = (FunctionInput) event.getData();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    Map<String, Object> eventMap = objectMapper.convertValue(event, Map.class);
    System.out.print(eventMap);

    String accountName = PropertyUtils.getProperty(eventMap, "data.accountName") != null ? BeanUtils.getNestedProperty(eventMap, "data.accountName") : "";
    String firstName = PropertyUtils.getProperty(eventMap, "data.firstName") != null ? BeanUtils.getNestedProperty(eventMap, "data.firstName") : "";
    String lastName = PropertyUtils.getProperty(eventMap, "data.lastName") != null ? BeanUtils.getNestedProperty(eventMap, "data.lastName") : "";
    String subject = PropertyUtils.getProperty(eventMap, "data.subject") != null ? BeanUtils.getNestedProperty(eventMap, "data.subject") : "";
    String description = PropertyUtils.getProperty(eventMap, "data.description") != null ? BeanUtils.getNestedProperty(eventMap, "data.description") : "";

    DataApi dataApi = context.getOrg().get().getDataApi();

    // Create a Unit of Work that inserts multiple objects
    UnitOfWorkBuilder unitOfWork = dataApi.newUnitOfWorkBuilder();

    // You can use the DataApi to create a new Record
    Record account = dataApi.newRecordBuilder("Account").withField("Name", accountName).build();
    // A ReferenceId will be returned to assign relationships with other objects within the same
    // transaction
    ReferenceId accountRefId = unitOfWork.registerCreate(account);

    Record contact =
        dataApi
            .newRecordBuilder("Contact")
            .withField("FirstName", firstName)
            .withField("LastName", lastName)
            .withField("AccountId", accountRefId)
            .build();
    ReferenceId contactRefId = unitOfWork.registerCreate(contact);

    // Here we are using the accountRefId and contactRefId to specify the relationship with the
    // temporary Id's created by the Unit of Work builder
    Record serviceCase =
        dataApi
            .newRecordBuilder("Case")
            .withField("Subject", subject)
            .withField("Description", description)
            .withField("Origin", "Web")
            .withField("Status", "New")
            .withField("AccountId", accountRefId)
            .withField("ContactId", contactRefId)
            .build();
    ReferenceId serviceCaseRefId = unitOfWork.registerCreate(serviceCase);

    Record followupCase =
        dataApi
            .newRecordBuilder("Case")
            .withField("ParentId", serviceCaseRefId)
            .withField("Subject", "Follow Up")
            .withField("Description", "Follow up with Customer")
            .withField("Origin", "Web")
            .withField("Status", "New")
            .withField("AccountId", accountRefId)
            .withField("ContactId", contactRefId)
            .build();
    ReferenceId followupCaseRefId = unitOfWork.registerCreate(followupCase);

    // The transaction will be committed and all the objects are going to be created.
    // The resulting map contains the Id's of the created objects
    Map<ReferenceId, RecordModificationResult> result =
        dataApi.commitUnitOfWork(unitOfWork.build());

    LOGGER.info("Function successfully commited UoW with {} affected records!", result.size());

    fc.fc_log_invocation_data("");
    // Construct the result by getting de Id's from the created objects
    return new FunctionOutput(
        result.get(accountRefId).getId(),
        result.get(contactRefId).getId(),
        new Cases(result.get(serviceCaseRefId).getId(), result.get(followupCaseRefId).getId()));
  }
}
