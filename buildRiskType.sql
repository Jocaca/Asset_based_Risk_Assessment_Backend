INSERT INTO risk_type (typeid, content, warning)  
VALUES 
-- Software Section
-- Q1.1
    (1,'Unauthorized software may contain unpatched vulnerabilities or malware','Software unauthorized and wrong software status in inventory.'),
    (2,'Unapproved software could violate license terms','Software unauthorized and wrong software status in inventory.'),
    (3,'Resource wasted (budget, IT Team, etc)','Software unauthorized and wrong software status in inventory.'),
-- Q2
    (4,'Unauthorized versions of the software may contain malware','Software is not whitelisted'),
    (5,'Unapproved versions may have unaddressed CVEs','Software is not whitelisted'),
    (6,'Untested versions can conflict with other apps','Software is not whitelisted'),
-- Q3
    (7,'Missed zero-day or known CVEs','Authenticated scan is not performed '),
    (8,'Fails audits','Authenticated scan is not performed '),
-- Q4
    (9,'Unauthenticated scans miss unpatched software or misconfigurations','Software is not scanned for vulnerabilities within a reasonable time period.'),
    (10,'Violates mandates requiring authenticated scans (e.g., PCI DSS 11.2.1, NIST 800-53)','Software is not scanned for vulnerabilities within a reasonable time period.'),
    (11,'Privilege Escalation Risks','Software is not scanned for vulnerabilities within a reasonable time period.'),
-- Q5
    (12,'Manual patching often lags vendor releases by weeks/months, Extended exposure to exploited CVEs','Updates are not fully automated.'),
    (13,'May fails regulation requirements like: PCI DSS (patch critical vulnerabilities within 30 days). HIPAA (security updates)','Updates are not fully automated.'),
-- Q6
    (14,'May assume vulnerabilities are fixed when they persist','Vulnerability scan results for the software is not compared regularly to confirm timely remediation'),
    (15,'Violates mandates requiring remediation proof','Vulnerability scan results for the software is not compared regularly to confirm timely remediation'),
-- Q7
    (16,'Systems/software are configured differently across the organization. Some instances may have critical security gaps','Documented security configuration standards not strictly maintained or enforced.'),
    (17,'Compliance Violations','Documented security configuration standards not strictly maintained or enforced.'),
    (18,'Increased Attack Surface','Documented security configuration standards not strictly maintained or enforced.'),
    (19,'Operational Inefficiency such as longer troubleshooting process','Documented security configuration standards not strictly maintained or enforced.'),
-- Q8
    (20,'Inconsistent Security Posture. Manual deployments lead to configuration drift','Secure images not always maintained.'),
    (21,'Compliance Violations','Secure images not always maintained.'),
    (22,'Slower Incident Response. Compromised systems rebuilt manually vs. automated reimaging','Secure images not always maintained.'),
    (23,'Higher Operational Costs. Manual deployments require 3-5x more effort','Secure images not always maintained.'),
-- Q9
    (24,'Unauthorized changes and compromised security Images','Security images are not fully secured.'),
-- Q10
    (25,'Configuration Drift. Systems gradually deviate from baselines','Configuration Enforcement are not fully automatic'),
    (26,'Delayed Response to Threats. Manual updates lag critical vulnerabilities','Configuration Enforcement are not fully automatic'),
    (27,'Compliance Failures','Configuration Enforcement are not fully automatic'),
-- Q11
    (28,'Undetected Misconfigurations','Not Using SCAP-Compliant Monitoring'),
    (29,'Unauthorized Changes','Not Using SCAP-Compliant Monitoring'),
    (30,'Compliance Failures','Not Using SCAP-Compliant Monitoring'),
    (31,'Lack of Audit Trails','Not Using SCAP-Compliant Monitoring'),
-- Q12.1
    (32,'Malware & Data Theft. Malicious plugins','Unauthorized Plugins/Add-Ons may exist.'),
-- Q12.2
    (33,'Malware & Exploits. Malicious scripts','Uncontrolled Script Execution may exist.'),
    (34,'Phishing & Credential Theft. Fake login pages (e.g., JavaScript-based "Office 365" phishing) steal credentials','Uncontrolled Script Execution may exist.'),
-- Q13.1
    (35,'Vulnerable Applications. Common flaws like SQLi, XSS, or buffer overflows lead to breaches','Insecure Coding Practices'),
    (36,'Compliance Failures','Insecure Coding Practices'),
    (37,'Increased Remediation Costs','Insecure Coding Practices'),
-- Q13.2
    (38,'Injection Attacks','Poor Input Validation'),
    (39,'Buffer Overflows. Unbounded input crashes systems or executes arbitrary code','Poor Input Validation'),
    (40,'Data Corruption. Invalid data types/ranges cause processing errors','Poor Input Validation'),
    (41,'Compliance Failures','Poor Input Validation'),
-- Q13.3
    (42,'Supply Chain Attacks. Malicious packages',' Untrusted/Outdated Components may be used.'),
    (43,'License & Compliance Violations',' Untrusted/Outdated Components may be used.'),
-- Q13.4
    (44,'Cryptographic Breakage','Non-Standard/Deprecated Encryption used.'),
    (45,'Compliance Violations','Non-Standard/Deprecated Encryption used.'),
    (46,'Data Exposure','Non-Standard/Deprecated Encryption used.'),
    (47,'Interoperability Failures','Non-Standard/Deprecated Encryption used.'),
-- Q13.5
    (48,'Proliferation of Common Vulnerabilities. Developers reintroduce SQLi, XSS, or insecure API flaws','Insufficient Secure Coding Training'),
    (49,'Compliance Failures','Insufficient Secure Coding Training'),
-- Q13.6
    (50,'Undetected Vulnerabilities in Production','Not or partially Using SAST/DAST'),
    (51,'Compliance Violations','Not or partially Using SAST/DAST'),
    (52,'Delayed Remediation Costs','Not or partially Using SAST/DAST'),
    (53,'Developer Inefficiency','Not or partially Using SAST/DAST'),
-- Q13.7
    (54,'Accidental Production Disruptions','Poor Environment Segregation'),
    (55,'Malicious Insider Threats','Poor Environment Segregation'),
    (56,'Compliance Violations','Poor Environment Segregation'),
    (57,'Data Corruption/Leaks. Test scripts using real customer data (e.g., GDPR breach)','Poor Environment Segregation'),
-- Q14
    (58,'Unpatched Vulnerabilities','Unsupported/Unhardened Software'),
    (59,'Increased Attack Surface. Unsupported software lacks security fixes','Unsupported/Unhardened Software'),
    (60,'Operational Failures. Compatibility issues or crashes due to outdated dependencies','Unsupported/Unhardened Software'),
-- Q15
    (61,'Unauthorized Access','No deployment of  Application Firewalls'),
    (62,'Increased Attack Surface','No deployment of  Application Firewalls'),
    (63,'Malware Infections','No deployment of  Application Firewalls'),
    (64,'Compliance Violations','No deployment of  Application Firewalls'),

-- Physical Section
-- Q1 (Fixed Asset)
    (65,'Loss of assets','Failure to register assets can lead to management confusion'),
    (66,'Duplicate purchases','Failure to register assets can lead to management confusion'),
    (67,'Failed audits','Failure to register assets can lead to management confusion'),
-- Q1.1
    (68,'Unable to locate assets','Missing key information may affect tracking'),
    (69,'Security vulnerabilities (e.g., unknown server location)','Missing key information may affect tracking'),
-- Q2
    (70,'Sudden downtime, production interruptions','Failure to maintain may result in equipment failure'),
    (71,'Security breaches (e.g., unpatched servers)','Failure to maintain may result in equipment failure'),
-- Q2.1
    (72,'Insufficient resources','Maintenance delays can hide bigger problems'),
    (73,'Missing Records','Maintenance delays can hide bigger problems'),
-- Q3
    (74,'Fines (e.g., Microsoft audits)','Unauthorized software can lead to legal risks'),
    (75,'Security breaches (e.g., industrial control software that has not been updated)','Unauthorized software can lead to legal risks'),
-- Q3.1
    (76,'Long-term non-compliance and audit failure','No corrective action plan may lead to continued violations'),
-- Q4
    (77,'Inadequate security measures (e.g., firewalls not upgraded)','Underestimation of risk levels can lead to insufficient protection'),
-- Q5
    (78,'Device theft','Improper storage may result in theft or destruction'),
    (79,'Data leakage (e.g., direct removal of hard drives)','Improper storage may result in theft or destruction'),
-- Q5.1
    (80,'Data breaches','Exposure of sensitive data can lead to significant losses'),
    (81,'Compliance penalties (e.g. GDPR)','Exposure of sensitive data can lead to significant losses'),
-- Non-Fixed Asset Q1
    (82,'Data can be read directly when the device is lost (e.g., customer information leakage)','Unencrypted devices can lead to data breaches'),
-- Q1.1
    (83,'Compliance penalties (e.g. GDPR), Reputational damage','Sensitive data is at extremely high risk of not being encrypted'),
-- Q2
    (84,'Long-term exposure of data after device loss','Failure to remotely wipe may result in data not being reclaimable'),
-- Q2.1
    (85,'Frequent losses lead to data breaches','Devices with high loss rates require stronger security measures'),
-- Q3
    (86,'The device cannot be held accountable after it is lost and is idle for a long time','Liability may be unclear due to incorrect information on the holder'),
-- Q3.1
    (87,'Increased risk of asset loss and data breaches','A long period of non-return can mean loss'),
-- Q4
    (88,'Reduced productivity due to the use of faulty equipment','Device damage may affect functionality or safety'),
-- Q4.1
    (89,'Unusable equipment (e.g., malfunction of medical equipment)','Critical Functional Compromise Could Lead to Business Disruption'),

-- Information Section
-- Database Asset
-- Q1
    (90,'Data breach, regulatory fines','Sensitive data is unencrypted, risking exposure to breaches.'),
-- Q2
    (91,'Unable to trace breaches',' Insufficient log retention hinders forensic investigations and compliance.'),
-- Q3
    (92,'Unauthorized privilege escalation','Admin accounts lack MFA, increasing risk of unauthorized access.'),
-- Q4
    (93,'Data corruption, exfiltration','Missing SQLi protections leave the database vulnerable to attacks.'),
-- Q5
    (94,'Exploitable vulnerabilities','Delayed patching exposes databases to known vulnerabilities.'),
-- Q6
    (95,'Failed disaster recovery','Untested backups may fail during recovery, risking data loss.'),
-- Q7
    (96,'GDPR non-compliance','PII in test environments violates privacy and compliance.'),
-- Q8
    (97,'Regional outage impact','Lack of geo-isolation risks data loss in regional outages.'),
-- Q9
    (98,'Third-party breaches','Unverified cloud vendors may have inadequate security controls.'),
-- Q10
    (99,'Operational inefficiencies','Undocumented schemas complicate audits and data governance.'),
-- Q11
    (100,'Test data leakage','Unmasked sensitive data in test environments increases exposure risk.'),
-- Q12
    (101,'Brute force attacks','No account lockout allows brute-force attacks on credentials.'),

-- Document Asset
-- Q1
    (102,'Accidental sharing','Unmarked documents increase risk of mishandling or unauthorized sharing.'),
-- Q2
    (103,'Data leakage via search','OCR-enabled scans allow text extraction, risking sensitive data exposure.'),
-- Q3
    (104,'Persistent unauthorized access','Permanent links raise the risk of unauthorized access over time.'),
-- Q4
    (105,'Disputed authenticity','Lack of immutable timestamps weakens legal document integrity.'),
-- Q5
    (106,'Physical document theft','Untracked hard copies risk loss or unauthorized distribution.'),
-- Q6
    (107,'Compliance violations','Manual retention processes risk compliance violations or data hoarding.'),
-- Q7
    (108,'No audit trail','Unlogged access obscures accountability for breaches or misuse.'),
-- Q8
    (109,'Interception risk','Unprotected email attachments risk interception or unauthorized access.'),
-- Q9
    (110,'Loss of revision data','Missing version control complicates audits and recovery of changes.'),
-- Q10
    (111,'Inconsistent formats','Decentralized templates risk inconsistency or outdated versions in use.'),
-- Q11
    (112,'Insider threats','Untrained staff increase risks of mishandling or non-compliance.'),

-- Patent Asset
-- Q1
    (113,'Limited IP coverage','Missing PCT filings limit international patent protection and market opportunities.'),
-- Q2
    (114,'Litigation surprises','No monitoring increases vulnerability to litigation from patent assertion entities.'),
-- Q3
    (115,'Trade secret leakage','Unencrypted disclosures risk premature exposure or theft of proprietary ideas.'),
-- Q4
    (116,'Loss of patent rights','Untracked fees risk accidental patent expiration and loss of rights.'),
-- Q5
    (117,'Ownership disputes','Missing agreements create ownership ambiguities for company inventions.'),
-- Q6
    (118,'Weak protection','Unreviewed claims may have weak enforcement or loopholes.'),
-- Q7
    (119,'Delayed enforcement','No reporting system delays infringement detection and legal action.'),
-- Q8
    (120,'Loss of priority date','Unvalidated dates risk losing priority rights in disputes.'),
-- Q9
    (121,'Management chaos','Disorganized tracking hinders portfolio management and licensing opportunities.'),
-- Q10
    (122,'Legal conflicts','Undefined terms risk disputes over rights, licensing, or revenue sharing.'),
-- Q11
    (123,'Export control violations','Missing licenses violate export controls and risk penalties.'),

-- People Section
-- Q1
    (124,'Incorrect identification, potential confusion in records, and access control issues','Failure to document the employee\'s full name may lead to identification issues and mismanagement.'),
-- Q2
    (125,'Unauthorized access, confusion in employee records, and potential security vulnerabilities','Missing or incorrect identifiers can cause data discrepancies and access control issues.'),
-- Q3
    (126,'Misallocation of responsibilities, potential internal security breaches, or regulatory violations','Failure to update department information may lead to misallocated resources or unauthorized access.'),
-- Q4
    (127,'Role confusion, unauthorized access, and improper handling of responsibilities','Unclear job roles may cause internal confusion and security vulnerabilities.'),
-- Q5
    (128,'Unauthorized access to sensitive data or systems','Misaligned access control may grant unauthorized privileges to employees.'),
-- Q6
    (129,'Increased likelihood of data breaches, security missteps, or internal threats','Lack of relevant training can result in human errors or security breaches.'),
-- Q7
    (130,'Security breaches due to outdated access privileges','Outdated clearance can lead to security vulnerabilities and unauthorized data access.'),
-- Q8
    (131,'Unauthorized access to sensitive assets and improper tracking','Failure to maintain a list of primary assets may result in mismanagement and security risks.'),
-- Q9
    (132,'Misuse of assets and improper allocation','Failure to document asset purposes can result in misallocation and security issues.'),
-- Q10
    (133,'Mismanagement of assets and lack of accountability','Failure to define asset responsibilities can lead to confusion and accountability gaps.'),
-- Q11
    (134,'Increased exposure to security vulnerabilities and policy violations','Failure to document remote work agreements can lead to security risks and regulatory non-compliance.'),
-- Q12
    (135,'Unmonitored asset use and data access, leading to potential misuse or breaches','Failure to audit access and asset data regularly can result in untracked changes and security risks.'),
-- Q13
    (136,'Delayed detection of threats, inadequate response to incidents, and potential data loss','Failure to maintain security incident records can delay incident response and post-event analysis.'),
-- Q14
    (137,'Lack of issue resolution, leading to repeated security vulnerabilities','Failure to review or analyze security incidents may lead to recurring issues.'),
-- Q15
    (138,'Abuse of access privileges, improper system access','Failure to regularly review and adjust access may lead to unnecessary security gaps.'),
-- Q16
    (139,'Unauthorized system access, untracked changes, and potential data exposure','Lack of an access approver increases the risk of unauthorized access and security breaches.'),
-- Q17
    (140,'Improper response to security events, leading to data leaks or system damage','Lack of security drills may leave employees unprepared for real attacks.'),
-- Q18
    (141,'Social engineering attacks may succeed, leading to system and data breaches','Lack of awareness of social engineering attacks can lead to information leakage or internal threats.'),
-- Q19
    (142,'Access rights not updated, leading to unauthorized access and asset misuse','Failure to update access rights and asset data after role changes can cause unnecessary security risks.'),
-- Q20
    (143,'Misallocation of employee resources, which may affect work efficiency and security','Failure to record workplace may lead to improper resource allocation or access errors.');