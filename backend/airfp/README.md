Backend – AI RFP Automation (Spring Boot)

This backend powers the full RFP automation workflow:
RFP extraction → Sending Vendor Proposal → Email polling → AI evaluation → recommendations.

### IMPORTANT HOW TO RUN THE APPLICAITON - START ###

Required Configurations
To run the backend application, you must configure:

MS SQL Server -> 1. Database connection string
                 2. Database Username
                 3. Database Password
                 
Mail Server ->  1. SMTP (Username,Password,Host,Port)
                2. IMAP (Username,Password,Host,Port)

LLM ------>     1. API Key for LLM
                     https://drive.google.com/file/d/1frGqAbYgtmEEx3bg3zeKok_tms3oNJ_k/view?usp=sharing
                     [use my API KEY]
                2. API URL

Download Speeh to Text Model from my google drive
https://drive.google.com/drive/folders/1i_bGT1Z-jWhtMLcmpX2LbotTbSwQYlbe?usp=sharing

src/main/resources/STTmodel/vosk-model-small-en-us-0.15 -> make sure model folder end this name after downloading it. [remove timestap after 0.15 if any]


How to run :

Step 1: Set Up Speech-to-Text (Vosk)
       Download Vosk STT lightweight model,Place inside /src/main/resources/STTmodel/

Step 2: Create application-secret.properties  inside /src/main/resources

       #Database
       DB_URL=
       DB_USERNAME=
       DB_PASSWORD=

       #API
       API_KEY=
       API_URL=

       #SMTP
       SMTP_HOST=
       SMTP_PORT=
       SMTP_USERNAME=
       SMTP_PASSWORD=

       #IMAP
       IMAP_HOST=
       IMAP_USERNAME=
       IMAP_PASSWORD=
       IMAP_PORT=
    
Step 3: Running the application
open terminal inside backend/airfp

      a. mvn clean install
      b. mvn spring-boot:run

Note : If you are using IntelliJ or any IDE , start the main class[AirfpApplication.java]


backend spring boot application will start running on http://localhost:8080/
         

### IMPORTANT HOW TO RUN THE APPLICAITON -- END ###


Now you can run frontend app:
go to fronend/README file and follow istructions


Main Controllers :

1. RFPController
2. VendorController
3. DashboardController


Important Services :

1. RfpExtractionService   
2. VedorProposalService
3. VendorMessagingService
4. EmailPollingService
5. EmailNormalizationService
6. EmailReceiverService
7. VendorEvaluationService
8. OpenRouterClient
9. VoiceToTextService


Key Endpoints :

POST /api/rfp/extract
POST /api/rfp/create
GET /api/rfp/{id}
Send to Vendor
POST /api/rfp/send

IMAP Polling for Vendor Responses
Runs every 30 seconds
Stores vendor response JSON in DB.


Database Schema 
RFP
RFP_ITEMS
VENDORS
VENDOR_SENT_RECORD
VENDOR_RESPONSE
VENDOR_RANKING



Vendor Evaluation Flow

Collect vendor responses
Build detailed prompt
Send to LLM
Parse JSON using Jackson
build RecommendationResult + rankings
Return final recommendation to frontend

