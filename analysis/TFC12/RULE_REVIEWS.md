# Rule reviews — TFC12

Recorded via `/modernize-review TFC12` (flagged scope). Answers feed the brief behavior contract; `BUSINESS_RULES.md` is not edited.

**Flagged inventory:** No P0 rules had suspected defects, SME questions, or sub-High confidence. One **P2** rule appeared under “Rules requiring SME confirmation” (**RULE-017**); it was reviewed below.

| Rule | Verdict | When | Title | Note |
|------|---------|------|-------|------|
| RULE-017 | confirmed | 2026-09-30T14:35:00+02:00 | F115L050 update-timestamp check disabled | Match legacy snapshot: do not enforce E000 on main path in Java. |

**Open `discuss` / `wrong` verdicts:** none.

All **14 P0** rules remain **High** confidence from extract-rules; no additional SME gate required before brief.

**Next:** `/modernize-brief TFC12 java21-spring-boot`
