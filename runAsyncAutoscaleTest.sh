#!/bin/bash
# Ask users for count
echo "How many invocations do you want to run?"
read seconds
c=1
remain=50
rm *.log
while [ $c -le $seconds ]; do 
    sfdx force:apex:execute -f test_apex/runAutoScaleAsync.apex -u test-uet4cvemcwup@example.com >> outAsyncAutoScale.log &
    sleep 5
    echo 'Another run that queues a function invocation.' $c 'out of' $seconds 'invokes started.' 
    c=$(($c+1))

done