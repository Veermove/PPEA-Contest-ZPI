FROM gradle:8.3-alpine

COPY . .
RUN gradle clean build

CMD gradle bootRun