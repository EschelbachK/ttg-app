FROM ubuntu:latest
LABEL authors="kesch"

ENTRYPOINT ["top", "-b"]