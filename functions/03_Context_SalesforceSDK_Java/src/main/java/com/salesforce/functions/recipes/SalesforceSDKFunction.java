package com.salesforce.functions.recipes;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.DataApi;
import com.salesforce.functions.jvm.sdk.data.Record;
import com.salesforce.functions.jvm.sdk.data.RecordModificationResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Map;
import logger.FCLogger;

/**
 * This function takes a payload containing account details, and creates the record. It then uses a
 * SOQL query to return the newly created Account.
 */
public class SalesforceSDKFunction implements SalesforceFunction<Object, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(SalesforceSDKFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<Object> event, Context context)
      throws Exception {

    FCLogger fc = new FCLogger(event, context, LOGGER);
    // FunctionInput fiObject = (FunctionInput) event.getData();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    Map<String, Object> eventMap = objectMapper.convertValue(event, Map.class);
    System.out.print(eventMap);

    // Retrieve payload fields
    String accountName = PropertyUtils.getProperty(eventMap, "data.name") != null ? BeanUtils.getNestedProperty(eventMap, "data.name") : "";

    if (accountName == null) {
      throw new Exception("Account Name is required");
    }

    String accountNumber = PropertyUtils.getProperty(eventMap, "data.accountNumber") != null ? BeanUtils.getNestedProperty(eventMap, "data.accountNumber") : "";
    String industry = PropertyUtils.getProperty(eventMap, "data.industry") != null ? BeanUtils.getNestedProperty(eventMap, "data.industry") : "";
    String type = PropertyUtils.getProperty(eventMap, "data.type") != null ? BeanUtils.getNestedProperty(eventMap, "data.type") : "";
    String website = PropertyUtils.getProperty(eventMap, "data.website") != null ? BeanUtils.getNestedProperty(eventMap, "data.website") : "";

    // Insert the record using the SalesforceSDK DataApi and get the new Record Id from the result
    DataApi dataApi = context.getOrg().get().getDataApi();

    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    String accountNameWithTimestamp = String.format("%s-%s", accountName, timeStamp);

    Record account =
        dataApi
            .newRecordBuilder("Account")
            .withField("Name", accountNameWithTimestamp)
            .withField("AccountNumber", accountNumber)
            .withField("Industry", industry)
            .withField("Type", type)
            .withField("Website", website)
            .build();

    RecordModificationResult createResult = dataApi.create(account);

    // Query Accounts using the SalesforceSDK DataApi to verify that our new Account was created.
    String queryString =
        String.format("SELECT Id, Name FROM Account WHERE Id = '%s'", createResult.getId());
    List<Record> records = dataApi.query(queryString).getRecords();

    LOGGER.info("Function successfully queried {} account records!", records.size());

    List<Account> accounts = new ArrayList<>();
    for (Record record : records) {
      String id = record.getStringField("Id").get();
      String name = record.getStringField("Name").get();
      accounts.add(new Account(id, name));
    }

    fc.fc_log_invocation_data("");
    return new FunctionOutput(accounts);
  }
}
