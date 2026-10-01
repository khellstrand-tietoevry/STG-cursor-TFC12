# Business Rules — TFC12

At extraction: 18 confirmed rules (14 P0); later steps may add or correct rules below. Each citation was checked by a second agent that read the cited lines; 0 candidate rules were refuted and left out.

| ID | Name | Category | Priority | Source | Confidence |
|---|---|---|---|---|---|
| RULE-001 | F115L050 requires function and medium | Validation | P0 | `legacy/STG/TFC12/src/F115L050.src:164-175` | High |
| RULE-002 | Invalid status and operation-type combination | Validation | P0 | `legacy/STG/TFC12/src/F115L050.src:335-349` | High |
| RULE-003 | Permanent status blocks in-progress main contract | Validation | P0 | `legacy/STG/TFC12/src/F115L050.src:420-430` | High |
| RULE-004 | K mandatory envelope fields | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:166-218` | High |
| RULE-005 | Charge row requires Selected when Charge-Type present | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:542-559` | High |
| RULE-006 | Charge row requires amount or percent when type present | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:562-619` | High |
| RULE-007 | Charge row requires Paying-Party when type present | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:582-599` | High |
| RULE-008 | RETURN or EXPIRE requires uniform paying party side | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:704-735` | High |
| RULE-009 | SETTLE-ILC PRES-BANK-CHARGES payer must be CUSTM | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:743-762` | High |
| RULE-010 | ACCEPT-ILC paying party must be CUSTM | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:772-787` | High |
| RULE-011 | Selected charge requires Paying-Party indicator | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:833-838` | High |
| RULE-012 | K-module initial check calls F115L050 | Validation | P0 | `legacy/STG/TFC12/src/FTFCK120.src:975-988` | High |
| RULE-013 | F115L050 PA-Seq-No zero skips part amount reads | Validation | P1 | `legacy/STG/TFC12/src/F115L050.src:106-114` | High |
| RULE-014 | L creates shadow when main contract not in progress | Lifecycle | P1 | `legacy/STG/TFC12/src/FTFCL120.src:184-187` | High |
| RULE-015 | H invokes K then L in sequence | Policy | P0 | `legacy/STG/TFC12/src/FTFCH120.src:103-104` | High |
| RULE-016 | Decimal point is comma | Policy | P0 | `legacy/STG/TFC12/src/FTFCH120.src:20` | High |
| RULE-017 | F115L050 update-timestamp check disabled | Policy | P2 | `legacy/STG/TFC12/src/F115L050.src:119-123` | Medium |
| RULE-018 | H-module must not be hand-edited | Policy | P2 | `legacy/STG/TFC12/src/FTFCH120.src:10` | High |

## Validation

### RULE-001: F115L050 requires function and medium
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/F115L050.src:164-175`
**Plain English:** Initial check abends if Function or Medium is spaces.
**Specification:**
  Given F115L050 after C100-Initialize-Output
  When  R115L050-Function or R115L050-Medium is spaces
  Then  TF-SY-FUNC-AND-MEDIUM; Z900-Abend paragraph C102
**Confidence:** High

### RULE-002: Invalid status and operation-type combination
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/F115L050.src:335-349`
**Plain English:** F115ISR0 VALID-ST-OP relation failure yields TF-SY-INVALID-STATUS-OPERTYP.
**Specification:**
  Given F100-Read-Sys-Code-Rel
  When  R7919999-Return-Error from F115ISR0
  Then  TF-SY-INVALID-STATUS-OPERTYP with operation and status variables; Z700-Error-AE
**Confidence:** High

### RULE-003: Permanent status blocks in-progress main contract
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/F115L050.src:420-430`
**Plain English:** If STATUS property contains PERM and main contract In-Progress-Yes, reject unless Function is TFD05.
**Specification:**
  Given G000-Check-Properties
  When  Ws-Code-Count > 0 and R115IMC0-In-Progress-Yes and Function not TFD05
  Then  TF-SY-MC-INPROG-Y; Z700-Error-AE
**Confidence:** High

### RULE-004: K mandatory envelope fields
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:166-218`
**Plain English:** K rejects missing Fin-Inst-No, Contract-Type, Contract-No, Last-Saved-Date, or Operation-Type when the respective *-I indicator is not 'U'.
**Specification:**
  Given C100-Mandatory-Fields
  When  Required RTFCE120 field is absent while indicator demands input
  Then  GL-ANGI-FELT& with Norwegian field label; Z700-Error-AE
**Parameters:** {"messageCode":"GL-ANGI-FELT&"}
**Confidence:** High

### RULE-005: Charge row requires Selected when Charge-Type present
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:542-559`
**Plain English:** If Charge-Type-I is not 'U' but Selected-I is 'U', K raises TF-SY-FIELD2-AND-FIELD1 (Omkostnings-type / Valgt).
**Specification:**
  Given C500-Dependencies charge table loop
  When  Charge-Type-I not 'U' and Selected-I = 'U'
  Then  TF-SY-FIELD2-AND-FIELD1 and Z700-Error-AE
**Confidence:** High

### RULE-006: Charge row requires amount or percent when type present
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:562-619`
**Plain English:** When Charge-Type-I is not 'U', Charge-Amount-I and Percent-Comm-I cannot both be 'U' without error (separate dependency checks for Beløp and Prosent-Provisjon).
**Specification:**
  Given C500-Dependencies
  When  Charge-Type-I not 'U' and Amount-I = 'U' (or Percent-I = 'U' in parallel check)
  Then  TF-SY-FIELD2-AND-FIELD1 with field labels; Z700-Error-AE
**Edge cases handled:** Separate loops for amount and percent — both must be satisfied when type is supplied
**Confidence:** High

### RULE-007: Charge row requires Paying-Party when type present
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:582-599`
**Plain English:** When Charge-Type-I is not 'U', Paying-Party-I must not be 'U'.
**Specification:**
  Given C500-Dependencies
  When  Charge-Type-I not 'U' and Paying-Party-I = 'U'
  Then  TF-SY-FIELD2-AND-FIELD1 (Betalende-Part); Z700-Error-AE
**Confidence:** High

### RULE-008: RETURN or EXPIRE requires uniform paying party side
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:704-735`
**Plain English:** For RETURN or EXPIRE-* operations, all charge rows must target buyer side (DRWEE/CUSTM) or seller side (DRWER/BENEF), not a mix or other parties.
**Specification:**
  Given Operation-Type RETURN or EXPIRE-*
  When  Paying parties counted across charge rows
  Then  TF-SY-OP-RETURN-PAY-PTY if buyer and seller both used or any 'other' party appears
**Confidence:** High

### RULE-009: SETTLE-ILC PRES-BANK-CHARGES payer must be CUSTM
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:743-762`
**Plain English:** On SETTLE-ILC, PRES-BANK-CHARGES rows must have Paying-Party-D = CUSTM.
**Specification:**
  Given Operation-Type-D = SETTLE-ILC
  When  Charge-Type-D = PRES-BANK-CHARGES and Paying-Party-D not CUSTM
  Then  TF-SY-ROLE-VAR1-CH-VAR2-INV; Z700-Error-AE
**Confidence:** High

### RULE-010: ACCEPT-ILC paying party must be CUSTM
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:772-787`
**Plain English:** On ACCEPT-ILC, every charge row Paying-Party-D must be CUSTM.
**Specification:**
  Given Operation-Type-D = ACCEPT-ILC
  When  Any charge row Paying-Party-D not CUSTM
  Then  TF-SY-ROLE-VAR1-CH-VAR2-INV; Z700-Error-AE
**Confidence:** High

### RULE-011: Selected charge requires Paying-Party indicator
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:833-838`
**Plain English:** When Selected-D = 'Y', Paying-Party-I must be 'U' (value supplied).
**Specification:**
  Given C520-Dependencies-UA after CONTRACT-TYPE sys-code read
  When  Selected-D = Y and Paying-Party-I not 'U'
  Then  TF-SY-TYPE-MISSING; Z700-Error-AE
**Confidence:** High

### RULE-012: K-module initial check calls F115L050
**Category:** Validation
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCK120.src:975-988`
**Plain English:** K fills R115L050-Initial-Check from function/header and CALLs F115L050.
**Specification:**
  Given E100-Initial-Checks
  When  Initial check runs
  Then  F115L050 called; abend/error maps to E101/E102 with message FM
**Confidence:** High

### RULE-013: F115L050 PA-Seq-No zero skips part amount reads
**Category:** Validation
**Priority:** P1
**Source:** `legacy/STG/TFC12/src/F115L050.src:106-114`
**Plain English:** When PA-Seq-No = 0, status comes from main contract only; part amount reads skipped.
**Specification:**
  Given F115L050 main read path
  When  R115L050-PA-Seq-No = 0
  Then  Move R115IMC0-Status to Ws-Status without C250/C300
**Confidence:** High

## Lifecycle

### RULE-014: L creates shadow when main contract not in progress
**Category:** Lifecycle
**Priority:** P1
**Source:** `legacy/STG/TFC12/src/FTFCL120.src:184-187`
**Plain English:** L-module D000-Preparations calls G060-Create-Shadow when R115IMC0-In-Progress-No.
**Specification:**
  Given FTFCL120 after G030-Read-Main-Contract
  When  Main contract not in progress
  Then  Perform G060-Create-Shadow (F115L140 path)
**Confidence:** High

## Policy

### RULE-015: H invokes K then L in sequence
**Category:** Policy
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCH120.src:103-104`
**Plain English:** H-module always performs C000-Call-K-Module then D000-Call-L-Module after initialize.
**Specification:**
  Given FTFCH120 A000-Main
  When  Main path runs
  Then  FTFCK120 then FTFCL120 are CALLed with L-STG-Header, L-Input, R7919999-Error
**Edge cases handled:** L is invoked after K returns; K/L status handled in H via S000-Status-Code
**Confidence:** High

### RULE-016: Decimal point is comma
**Category:** Policy
**Priority:** P0
**Source:** `legacy/STG/TFC12/src/FTFCH120.src:20`
**Plain English:** H/K/L use European decimal comma via Special-Names.
**Specification:**
  Given FTFCH120, FTFCK120, or FTFCL120 Environment Division
  When  Numeric edited data is formatted
  Then  Decimal-Point Is Comma applies
**Confidence:** High

### RULE-017: F115L050 update-timestamp check disabled
**Category:** Policy
**Priority:** P2
**Source:** `legacy/STG/TFC12/src/F115L050.src:119-123`
**Plain English:** E000-Check-Upd-Timestamp block is commented out (July test note).
**Specification:**
  Given F115L050 after status reads
  When  Check-For-Upd-Yes would apply
  Then  E000 not performed
**Confidence:** Medium — Confirm whether production mainframe re-enabled update-timestamp validation.

### RULE-018: H-module must not be hand-edited
**Category:** Policy
**Priority:** P2
**Source:** `legacy/STG/TFC12/src/FTFCH120.src:10`
**Plain English:** Header states THIS MODULE MUST NOT BE MODIFIED BY HUMAN HANDS.
**Specification:**
  Given FTFCH120 Servo policy
  When  Maintenance targets H-module
  Then  Regenerate from Servo tooling; do not hand-edit
**Confidence:** High

## Rules requiring SME confirmation

- **RULE-017** (Medium): Confirm whether production mainframe re-enabled update-timestamp validation.

## ⚠ Instruction-shaped content found in source

These lines of the source tried to steer automated analysis. A person should look at them.

- legacy/STG/TFC12/src/FTFCH120.src:10 — MUST NOT BE MODIFIED BY HUMAN HANDS
- legacy/STG/TFC12/src/FTFCK120.src:11-12 — META: code between tags must not be modified
