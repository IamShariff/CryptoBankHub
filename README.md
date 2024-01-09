# CryptoBankHub 🌐💰

Welcome to CryptoBankHub, a microservices project that allows users to manage their bank accounts, trade cryptocurrencies, and stay updated with real-time crypto details. Users can also track their portfolio's profit and loss for a comprehensive financial overview.

## Features 🚀

- **User Registration**: Users can easily register and create an account in CryptoBankHub.

- **Bank Account Management**: Users can add and manage their bank accounts within the system.

- **Real-Time Crypto Details**: Stay informed with real-time updates on cryptocurrency values.

- **Buy and Sell Cryptocurrencies**: Execute buy and sell orders seamlessly within the platform.

- **Transaction History**: Track and view detailed transaction history for better financial management.

- **Portfolio Profit and Loss Tracking**: Monitor the performance of your cryptocurrency investments with a detailed view of profit and loss.

- **Authentication with Keycloak**: Secure user authentication and authorization using Keycloak.

- **Asynchronous Communication with Kafka**: Utilize Kafka for efficient inter-service communication.

- **API Gateway with Spring Cloud Gateway**: Manage and route API requests effectively.

- **Java Mail for Email Notifications**: Send email notifications for various user activities.

## Getting Started 🛠️

### Prerequisites 📋

- [Keycloak](https://www.keycloak.org/) installed and configured with a realm and client details.

- [Apache Kafka](https://kafka.apache.org/) and [ZooKeeper](https://zookeeper.apache.org/) running.

### Configuration ⚙️

1. Clone the repository:

    ```bash
    git clone https://github.com/iammShariff/CryptoBankHub.git
    ```

2. Navigate to the `keycloakusermanagement` service and update the `application.yml` file with your Keycloak details.

3. Start Kafka and ZooKeeper services.

4. Run the CryptoBankHub microservices.

## Usage 🚦

1. Access the API Gateway at `http://localhost:1010`.

2. Register a new user and add bank accounts.

3. Explore real-time crypto details, buy and sell cryptocurrencies.

4. Check your transaction history for a detailed overview.

5. Monitor your portfolio's profit and loss for a comprehensive financial analysis.

## Contributors 🤝

- [Md Sharif](https://github.com/iammShariff)

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
