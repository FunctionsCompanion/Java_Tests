/**
 * Describe Apexsyslogjs here.
 *
 * The exported method is the entry point for your code when the function is invoked. 
 *
 * Following parameters are pre-configured and provided to your function on execution: 
 * @param event: represents the data associated with the occurrence of an event, and  
 *                 supporting metadata about the source of that occurrence.
 * @param context: represents the connection to Functions and your Salesforce org.
 * @param logger: logging handler used to capture application logs and trace specifically
 *                 to a given execution of a function.
 */
import * as fc from 'sf-fc-logger';
export default async function (event, context, logger) {
      try {

            fc.init(event, context, logger);
            logger.info(`${JSON.stringify(event.data || {})}`);
            fc.fc_log_invocation_data('fromApexSyslog');
            return;
      } catch (e) {
            fc.fc_log_invocation_data(e);
      }

}