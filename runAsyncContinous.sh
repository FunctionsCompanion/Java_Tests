#!/bin/bash
# Ask users for count
rm *.log
while [ 1 ]; do 
    sfdx force:apex:execute -f test_apex/runAutoScaleAsync.apex -u test-uet4cvemcwup@example.com >> /dev/null &
    sleep .5
done