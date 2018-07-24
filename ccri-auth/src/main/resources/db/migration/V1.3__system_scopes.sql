--
-- Turn off autocommit and start a transaction so that we can use the temp tables
--



START TRANSACTION;

--
-- Insert scope information into the temporary tables.
--

INSERT INTO system_scope (scope, description, icon, default_scope, restricted) VALUES
  ('openid', 'OpenID Connect id_token request', 'user', false, false),
  ('profile', 'User Profile Claim', 'list-alt', false, false),
  ('smart/orchestrate_launch', 'Launch orchestration', 'certificate', false, true),
  ('launch', 'Launch context info', true, true, false),
  ('launch/patient', 'When launching outside an EHR, provide patient context at time of launch', 'certificate', true, false),
  ('launch/encounter', 'When launching outside an EHR, provide encounter context at time of launch', 'certificate', false, false),
  ('launch/location', 'When launching outside an EHR, provide location context at time of launch', 'certificate', false, false),
  ('launch/other', 'Launch with other context', 'certificate', false, false),
  ('user/*.*', 'Read-write all data accessible by the user', 'certificate', false, false),
  ('user/*.read', 'Read all data accessible by the user', 'certificate', false, false),
  ('user/*.write', 'Write all data accessible by the user', 'certificate', false, false),
  ('user/AdverseReaction.read', 'Read all AdverseReactions accessible by the user', 'certificate', false, false),
  ('user/AdverseReaction.write', 'Write all AdverseReactions accessible by the user', 'certificate', false, false),
  ('user/Alert.read', 'Read all Alerts accessible by the user', 'certificate', false, false),
  ('user/Alert.write', 'Write all Alerts accessible by the user', 'certificate', false, false),
  ('user/AllergyIntolerance.read', 'Read all AllergyIntolerances accessible by the user', 'certificate', false, false),
  ('user/AllergyIntolerance.write', 'Read all AllergyIntolerances accessible by the user', 'certificate', false, false),
  ('user/Condition.read', 'Read all Conditions accessible by the user', 'certificate', false, false),
  ('user/Condition.write', 'Write all Conditions accessible by the user', 'certificate', false, false),
  ('user/Encounter.read', 'Read all Encounters accessible by the user', 'certificate', false, false),
  ('user/Encounter.write', 'Write all Encounters accessible by the user', 'certificate', false, false),
  ('user/FamilyHistory.read', 'Read all FamilyHistory accessible by the user', 'certificate', false, false),
  ('user/FamilyHistory.write', 'Write all FamilyHistory accessible by the user', 'certificate', false, false),
  ('user/Immunization.read', 'Read all Immunizations accessible by the user', 'certificate', false, false),
  ('user/Immunization.write', 'Write all Immunizations accessible by the user', 'certificate', false, false),
  ('user/MedicationOrder.read', 'Read all MedicationOrders accessible by the user', 'certificate', false, false),
  ('user/MedicationOrder.write', 'Write all MedicationOrders accessible by the user', 'certificate', false, false),
  ('user/Medication.read', 'Read all Medications accessible by the user', 'certificate', false, false),
  ('user/Medication.write', 'Write all Medications accessible by the user', 'certificate', false, false),
  ('user/MedicationPrescription.read', 'Read all MedicationPrescriptions accessible by the user', 'certificate', false, false),
  ('user/MedicationPrescription.write', 'Write all MedicationPrescriptions accessible by the user', 'certificate', false, false),
  ('user/MedicationStatement.read', 'Read all MedicationStatements accessible by the user', 'certificate', false, false),
  ('user/MedicationStatement.write', 'Write all MedicationStatements accessible by the user', 'certificate', false, false),
  ('user/Observation.read', 'Read all Observations accessible by the user', 'certificate', false, false),
  ('user/Observation.write', 'Write all Observations accessible by the user', 'certificate', false, false),
  ('user/Patient.read', 'Read all Patients accessible by the user', 'certificate', false, false),
  ('user/Patient.write', 'Write all Patients accessible by the user', 'certificate', false, false),
  ('user/Substance.read', 'Read all Substances accessible by the user', 'certificate', false, false),
  ('user/Substance.write', 'Write all Substances accessible by the user', 'certificate', false, false),
  ('patient/*.*', 'Read-write all data for a given patient', 'certificate', false, false),
  ('patient/*.read', 'Read all data for a given patient', 'certificate', false, false),
  ('patient/*.write', 'Write all data for a given patient', 'certificate', false, false),
  ('patient/AdverseReaction.read', 'Read all AdverseReactions for a given patient', 'certificate', false, false),
  ('patient/AdverseReaction.write', 'Write all AdverseReactions for a given patient', 'certificate', false, false),
  ('patient/Alert.read', 'Read all Alerts for a given patient', 'certificate', false, false),
  ('patient/Alert.write', 'Write all Alerts for a given patient', 'certificate', false, false),
  ('patient/AllergyIntolerance.read', 'Read all AllergyIntolerance for a given patient', 'certificate', false, false),
  ('patient/AllergyIntolerance.write', 'Write all AllergyIntolerance for a given patient', 'certificate', false, false),
  ('patient/Condition.read', 'Read all Conditions for a given patient', 'certificate', false, false),
  ('patient/Condition.write', 'Write all Conditions for a given patient', 'certificate', false, false),
  ('patient/Encounter.read', 'Read all Encounters for a given patient', 'certificate', false, false),
  ('patient/Encounter.write', 'Write all Encounters for a given patient', 'certificate', false, false),
  ('patient/FamilyHistory.read', 'Read all FamilyHistory for a given patient', 'certificate', false, false),
  ('patient/FamilyHistory.write', 'Write all FamilyHistory for a given patient', 'certificate', false, false),
  ('patient/Immunization.read', 'Read all Immunizations for a given patient', 'certificate', false, false),
  ('patient/Immunization.write', 'Write all Immunizations for a given patient', 'certificate', false, false),
  ('patient/MedicationOrder.read', 'Read all MedicationOrders for a given patient', 'certificate', false, false),
  ('patient/MedicationOrder.write', 'Write all MedicationOrders for a given patient', 'certificate', false, false),
  ('patient/MedicationPrescription.read', 'Read all MedicationPrescriptions for a given patient', 'certificate', false, false),
  ('patient/MedicationPrescription.write', 'Write all MedicationPrescriptions for a given patient', 'certificate', false, false),
  ('patient/MedicationStatement.read', 'Read all MedicationStatements for a given patient', 'certificate', false, false),
  ('patient/MedicationStatement.write', 'Write all MedicationStatements for a given patient', 'certificate', false, false),
  ('patient/Observation.read', 'Read all Observations for a given patient', 'certificate', false, false),
  ('patient/Observation.write', 'Write all Observations for a given patient', 'certificate', false, false),
  ('patient/Patient.read', 'Read all Patients for a given patient', 'certificate', false, false),
  ('patient/Patient.write', 'Write all Patients for a given patient', 'certificate', false, false),
  ('offline_access', 'Allows the app to maintain authorization even when the app is offline.', 'time', false, false),
  ('online_access', 'Allows the app to maintain authorization as long as the app is online.', 'globe', false, false);
COMMIT;

