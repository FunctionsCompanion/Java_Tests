#!/bin/bash
# Ask users for count
echo "Enter the number async invocations you want to run"
read jobs
c=1
remain=50
rm *.log
while [ $c -le $jobs ]; do 
    sfdx force:apex:execute -f test_apex/runAutoScaleAsync.apex -u test-uet4cvemcwup@example.com >> outAsyncAutoScale.log &
    sleep 20
    echo 'Another run that queues a function invocation.' $c 'out of' $jobs 'invokes started.' 
    c=$(($c+1))

done