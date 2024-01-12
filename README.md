Non-Code Modification Supervisory Control Software

#### Introduction:
Introducing a supervisory control software that requires no code modification, designed for monitoring upper-level machines of renewable energy devices such as UPS, inverters, and charging stations (not limited to).

#### Software Architecture:
In the communication of renewable energy devices, have you ever been tired of adapting to numerous machine models, matching various protocols, and maintaining a long list of if-else statements in your code until it becomes overwhelming? If so, this software is the solution you need. The design philosophy is simple: change configurations without changing the code. Once development is complete, minimal code changes are required for maintenance. How is this achieved? Through JSON! JSON, a special format of .txt files, forms the basis for both page and protocol configurations. Upon software startup, it reads relevant JSON configurations, generating corresponding pages and displaying information based on the configured JSON protocols. This means that for future protocol modifications or additions, you only need to modify the corresponding JSON protocol files.

#### Chinese Version
https://gitee.com/watson_bi/monitor/


#### Technologies and Tools:
Development Tools: IntelliJ IDEA, JDK 17, Gradle
Logic Processing: Java, with special use of the Jackson library for JSON handling
UI Framework: JavaFX
Database: H2 embedded database
Database Framework: MyBatis

#### Instructions:
1. No need to generate new pages using form-style drag controls or manually creating them with code. Simply copy and modify JSON files.

2. Adapting to new protocols requires no if-else statements. Just copy and modify the corresponding JSON files.

3. Adding new languages, such as English after initially having only Chinese, does not require code changes. Simply copy and modify JSON files.

4. The effectiveness of the above claims can be seen in the demonstration video. For implementation details, refer to the source code.

5. Why H2 Embedded Database?
   For standalone applications, the H2 database is sufficient—compact, convenient, and easy to integrate with jar dependencies. Additionally, if you wish to switch to another database, it's easy to do so by modifying some MyBatis configurations.

6. Communication Methods:
   While upper-level monitoring software and renewable energy devices can have multiple communication methods (serial port/USB/Modbus-RTC/Modbus-TCP/SNMP, etc.), this version only implements the logic for serial port communication. Users are encouraged to implement other communication methods based on their needs.

7. In this version, a simple history record table with pagination functionality has been implemented. For more table designs and various CRUD methods, users can implement as needed.

8. Why Gradle?
   Initially using Maven, Gradle was adopted to adapt to Java modularity. Gradle offers a plugin, badass-jlink-plugin, which automatically converts non-modular JARs to modular JARs. Why modularity? Traditional Java projects require the entire JRE, while modular projects only need a custom JRE with necessary modules, significantly reducing installation package size.

9. Platform Compatibility:
   Adapted for Windows/Mac/Linux, but the installation package is currently only available for Windows. Users can test on other platforms.

10. More Information:
    For additional details, feel free to contact via Weixin(Chinses version WeChat): huasheng-dali.

11. Personal Studio Services:
    A newly established studio welcomes custom software orders at negotiable prices, including but not limited to frontend/backend development, mobile app development, and supervisory control systems related to renewable energy.

#### Software Experience:
Two compressed files, msi.zip (installation package) and monitor.zip (standalone), are available in the master directory for installation and direct experience.

#### Screenshots:
![Dark Theme - English - Real-time Information](https://foruda.gitee.com/images/1705050807994610384/a92b5bb2_8975418.png)
![Light Theme - Chinese - Chart Display](https://foruda.gitee.com/images/1705050840613531256/5865f8ee_8975418.png)

#### Links:
Demonstration video Douyin link: https://v.douyin.com/iLj4HPpS/ IVL:/ 12/12 m@D.uF
