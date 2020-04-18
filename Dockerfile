FROM java:8

WORKDIR /

COPY target/gear-scanner-*.jar gear-scanner.jar

CMD java -cp gear-scanner.jar org.izolotov.crawler.CrawlerApp\
    --user-agent GearBot/0.1 \
    --delay 3000 \
    --timeout 10000 \
    --crawler-threads 12 \
    --sqs-max-miss-count 3 \
    --aws-region us-east-2 \
    --sqs-queue-name CrawlQueue \
    --sqs-wait-time 5 \
    --crawl-table products \
    --image-bucket-arn arn:aws:s3:us-east-2:848625190772:accesspoint/internet