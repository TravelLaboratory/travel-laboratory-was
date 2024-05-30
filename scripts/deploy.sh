#!/bin/bash

LOG_FILE="/home/ubuntu/deploy.log"

echo "> $(date) - 현재 실행 중인 Docker 컨테이너 pid 확인" >> $LOG_FILE
CURRENT_PID=$(sudo docker container ls -q)

if [ -z "$CURRENT_PID" ]
then
  echo "> $(date) - 현재 구동중인 Docker 컨테이너가 없으므로 종료하지 않습니다." >> $LOG_FILE
else
  echo "> $(date) - sudo docker stop start $CURRENT_PID"  >> $LOG_FILE # 현재 구동중인 Docker 컨테이너가 있다면 모두 중지
  sudo docker stop "$CURRENT_PID"
  echo "> $(date) - sudo docker stop finished $CURRENT_PID"  >> $LOG_FILE # 현재 구동중인 Docker 컨테이너가 있다면 모두 중지
  if [ $? -ne 0 ]; then
    echo "> $(date) - Docker 컨테이너 중지 실패" >> $LOG_FILE
    exit 1
  fi
  sleep 5
fi

cd /home/ubuntu/app || { echo "Failed to cd to /home/ubuntu/app"; exit 1; }

echo "> $(date) - Docker 이미지 빌드 시작" >> $LOG_FILE
sudo docker build -t travel-laboratory-docker .

echo "> $(date) - Docker 컨테이너 실행 시작" >> $LOG_FILE
sudo docker run -d -p 8080:8080 travel-laboratory-docker

echo "> $(date) - 배포 완료" >> $LOG_FILE