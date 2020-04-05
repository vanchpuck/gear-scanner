![Build Status](https://codebuild.us-east-2.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiNi9YVXJzRDUyaWtYUkVWS1NwUXh6MUo3QXdqVDU5SU1TRjdBdUlzYWFic25YVERnQXpHa29GTHZyampScVdDRnVST1Jpc1RISzVtWThwOUpBRnJtb1owPSIsIml2UGFyYW1ldGVyU3BlYyI6IkNMeEttNFdBdDBZa0RzcTMiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master)
## The GearScanner workflow
![Workflow schema](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/vanchpuck/gear-scanner/master/workflow.iuml?)

## How to run with ECS
`ecs-cli compose --project-name gear-scanner-crawler service up --create-log-groups --cluster-config GearScannerCrawlerConf --ecs-profile default`
