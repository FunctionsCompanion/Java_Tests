String Jsonpayload = '{"name": "MyAccount","accountNumber": "123456789","industry": "Technology","type": "Prospect","website": "www.salesforce.com"}';
FCFunction.getAndInvoke('Java_Tests.salesforcesdkjava', Jsonpayload, 'FCCallback');
