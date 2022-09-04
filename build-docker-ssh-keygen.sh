#!/usr/bin/env sh
mkdir -p ./dashboard/ssh ;
mkdir -p ./agent/ssh ;
rm -f ./agent/ssh/id_rsa_dashboard.pub ./dashboard/ssh/id_rsa_dashboard ./dashboard/ssh/id_rsa_dashboard.pub ;
ssh-keygen -q -t rsa -b 4096 -C "ssh@agent" -N "" -f ./dashboard/ssh/id_rsa_dashboard ;
cp ./dashboard/ssh/id_rsa_dashboard.pub ./agent/ssh/. ;
echo "./agent/ssh:" ; ls -lrt ./agent/ssh ;
echo "./dashboard/ssh:" ; ls -lrt ./dashboard/ssh ;
