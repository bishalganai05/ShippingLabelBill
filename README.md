# Shipping Label Bill Generator

This project is a Spring Boot application designed to generate professional shipping labels (bills) with integrated QR codes and barcodes, and then send them as email attachments to customers. It leverages Excel sheets for data storage, providing a lightweight alternative to traditional databases.

## Features

**PDF Generation**: Dynamically generates well-formatted PDF shipping labels from provided details using iText7.

**QR Code Integration**: Creates and embeds QR codes into the generated PDF for quick information retrieval.

**Barcode Integration**: Generates and adds barcodes to the PDF for efficient scanning and tracking.

**Excel Sheet Data Management**: Utilizes Excel sheets as the data source for shipping details, eliminating the need for a separate database.

**Email Delivery**: Sends the generated shipping bill as a PDF attachment to the customer's email address using Spring Boot Mail Sender API.

**Robust API Endpoints**: Implements proper ResponseEntity handling for clear API responses.

**Local PDF Storage**: Automatically saves a copy of the generated PDF shipping bill to a local directory.

## Technologies Used
This project is built with Spring Boot and utilizes the following key dependencies:

**Spring Boot Starter Web**: For building RESTful web applications.

**Spring Boot DevTools**: Provides fast application restarts and live reload for development.

**ZXing (Zebra Crossing) Core & JavaSE**: For generating QR codes and barcodes.

**iText7 Core**: A powerful PDF library for creating and manipulating PDF documents.

**Spring Boot Starter Mail**: For sending emails with attachments.

**Apache POI (poi-ooxml)**: For reading and manipulating data from Excel (XLSX) files.

---

## Getting Started

### Prerequisites

* Java 17 or higher
* Maven

### Installation and Running
1.  **Clone the Repository:**

    ```bash
    git clone https://github.com/bishalganai05/ShippingLabelBill.git
    cd ShippingLabelBill
    ```

2.  **Build the Project:**

    ```bash
    ./mvnw clean install
    ```

3.  **Run the Application:**

    ```bash
    ./mvnw spring-boot:run
    ```

    The application will start on `http://localhost:8080` (default port).

---

## 💻 API Endpoints

The primary endpoint for generating shipping labels expects a JSON payload.

### `POST /shipping-bill`

Generates a shipping label PDF, saves it locally, and sends it via email.

#### Request Body Example (JSON)

```json
{
  "fromAddress": "33 Ashram Road, Ahmedabad",
  "fromPIN": "380014",
  "toAddress": "40 MG Layout, Mysuru",
  "toPIN": "570001",
  "productID": "P010",
  "productName": "Laptop Stand",
  "productType": "Office",
  "emailID": "user10@example.com"
}
```
---

### Using Postman

1. Set the **request method** to `POST`.
2. Set the **URL** to `http://localhost:8080/shipping-bill`.
3. Go to the **Body** tab → Select **raw** → Choose **JSON** from the dropdown.
4. Paste the JSON payload.
5. Click **Send**.

### Future Enhancements

- **Microservices Integration:** The current services are designed with future integration into a larger microservices architecture in mind.
- **Database Integration:** While currently using Excel, future plans may include integration with a relational or NoSQL database for more robust data management.
- **UI Development:** Development of a user interface for easier interaction and management of shipping labels.
- **Batch Processing:** Functionality to generate multiple shipping labels from a batch of data.
