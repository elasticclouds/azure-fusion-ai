
-- Initial Applications Data
INSERT INTO LoanApplication
(loanNumber, loanType, customerName, email, address, isKYCVerified, loanAmount, loanStatus)
VALUES
('LN101', 'Auto Loan', 'Hari Panakkal', 'hariskumar@gmail.com', '123 Main St, City, Country', false, 25000.00, 'SUBMITTED');

INSERT INTO LoanApplication
(loanNumber, loanType, customerName, email, address, isKYCVerified, loanAmount, loanStatus)
VALUES
('LN102', 'Auto Loan', 'John Doe', 'john.doe@example.com', '456 Elm St, City, Country', false, 15000.00, 'SUBMITTED');

--- Initial Prompts
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P101', 'What is AI?'); 
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P102', 'What is Generative AI?'); 
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P103', 'Hey Genie, Please let me know what is the time now?'); 
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P104', 'What is Generative AI and How Semantic Kernel can be used to build it?');
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P105', 'Hey Genie, How can I apply for a loan application?');
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P106', 'Hey Genie, When will a loan application get approved?');
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P107', 'Hey Genie, Please get the application details for Loan Number LN101');
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P108', 'Hey Genie, I am a frustrated customer that my Loan application is not moving or not getting approved. Please help');
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P109', 'Hey Genie, My loan number is LN101, Please help');
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P110', 'Here is my updated address : One Microsoft Way, Redmond, WA');
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P111', 'Create a Loan Application for customerName Peter Pan email peter.pan@fusion.com address 123 Main St, City, Country for Auto Loan with Loan Amount 10000 and Loan Number LN103');
INSERT INTO Prompt (promptNumber, prompt) VALUES ('P112', 'Update the address 123 Main St, New York, NY for a given application loan number LN103');

