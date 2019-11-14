Load data
--CHARACTERSET UTF8
infile "union.txt"
APPEND INTO TABLE temp_UNIONFILEDATA_1
trailing NULLCOLS
(
UND_SETTLE_DT position(1) char(8),
UND_TRANS_ID position(*) char(3),
UND_SYS_TRA_NO position(*) char(6),
UND_TRANSMSN_DT_TM position(*) char(10),
UND_FLD_INS_ID_CD position(*) char(11),
UND_FWD_INS_ID_CD position(*) char(11),
UND_RCV_INS_ID_CD position(*) char(11),
UND_PRI_ACCT_NO position(*) char(19),
UND_TRANS_AT position(*) char(12),
UND_MCHNT_TP position(*) char(4),
UND_RETRI_REF_NO position(*) char(12),
UND_AUTH_ID_RESP_CD position(*) char(6),
UND_TERM_ID position(*) char(8),
UND_MCHNT_CD position(*) char(15),
UND_CARD_ACCPTR_NM_LOC position(*) char(40),
UND_MCHNT_DEBT_DISC_AT position(*) char(8),
UND_MCHNT_CRET_DISC_AT position(*) char(8),
UND_TRANS_ST position(*) char(5),
UND_TRAN_ACC position(*) char(19),
UND_PRO_FLAG position(*) char(1),
UND_CODE position(*) char(2)
)