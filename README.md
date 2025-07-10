# Project Title

The project is a Resilient email sending service that supports multiple providers with failover, retry logic, idempotency, rate limiting, and status tracking.


## Features

- Retry logic with exponential backoff
- Fallback to secondary provider
- Idempotency using unique id
- Rate limiting (5 requests/minute)
- Status tracking 
- Custom exception handling with http status


## Technologies

- Java 21
- Spring Boot
- Custom Exception Handling
- SLF4J for Logging


## Working

- User calls /api/send with email content 
- Service checks:
   - Rate limit for to email.
   - If this requestId was already processed (idempotency).
- Tries to send via Provider A.
   - Retries up to 3 times with exponential backoff.
- If A fails, uses Provider B.
- Updates email send status.
- /api/status/ shows the final status.



## API Reference

#### Send the mail

http
  POST /api/send


| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| emailRequest | EmailRequest | *Required*. Recipient email id, subject, body |

#### sendMail(emailRequest)

Takes recipient email id, subject and body of email and send the mail
#### Check status

http
  GET /api/status


| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| emailRequest      | EmailRequest | *Required*. Recipient email id, subject, body to fetch |

#### status(emailRequest)

Takes recipient email id, subject and body of email and return the status of that mail
