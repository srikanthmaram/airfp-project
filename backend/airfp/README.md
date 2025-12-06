Backend – AI RFP Automation (Spring Boot)

This backend powers the full RFP automation workflow:
RFP extraction → vendor notifications → email polling → AI evaluation → recommendations.

Run the Backend
./mvnw spring-boot:run


Backend runs on:

http://localhost:8080/api

Environment Variables

Create a file:

backend/src/main/resources/application-secret.properties


Add:

OPENROUTER_API_KEY=
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

MAIL_IMAP_HOST=
MAIL_IMAP_USER=
MAIL_IMAP_PASSWORD=

MAIL_SMTP_HOST=
MAIL_SMTP_USER=
MAIL_SMTP_PASSWORD=


Then include it from application.properties:

spring.config.import=optional:application-secret.properties

Key Endpoints
RFP

POST /api/rfp/extract

POST /api/rfp/create

GET /api/rfp/{id}

Send to Vendor

POST /api/rfp/send

IMAP Polling for Vendor Responses

Runs every X mins (cron scheduler)
Stores vendor response JSON in DB.

AI Evaluation

POST /api/rfp/{id}/evaluate
Uses LLM → saves RecommendationResult + VendorRanking.

Dashboard Summary

GET /api/dashboard/summary

Database Schema (Simplified)
RFP
RFP_ITEMS
VENDORS
VENDOR_SENT_RECORD
VENDOR_RESPONSE
RECOMMENDATION_RESULT
VENDOR_RANKING

Vendor Evaluation Flow

Collect vendor responses

Build detailed prompt

Send to LLM

Parse JSON using Jackson

Save RecommendationResult + rankings

Return final recommendation to frontend

