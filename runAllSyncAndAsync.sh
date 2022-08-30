#!/bin/bash
# Ask users for count
echo "Enter the number of queued jobs you want to run"
read jobs
c=1
remain=50
rm *.log
while [ $c -le $jobs ]; do 
    sfdx force:apex:execute -f test_apex/runAutoScaleAsync.apex -u test-uet4cvemcwup@example.com >> outAsyncAutoScale.log &
    sleep 20
    sfdx force:apex:execute -f test_apex/runAutoScaleSync.apex -u test-uet4cvemcwup@example.com  >> outSyncAutoScale.log &
    sleep 20
    echo 'Another 2 runs that queue function invocations.' $c 'out of' $jobs 'batches started.' 
    c=$(($c+1))

done