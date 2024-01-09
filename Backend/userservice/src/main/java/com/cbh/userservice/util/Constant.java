package com.cbh.userservice.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
	
	public static final String KYCSTATUS_NOTINITIALISED = "Not Initialzed";
	public static final String KYCSTATUS_APPLIED = "Applied";
	public static final String KYCSTATUS_APPROVED = "Approved";
	
}