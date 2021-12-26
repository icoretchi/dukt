package com.corex.erp.domain.so

import com.corex.erp.domain.shared.VatNo

data class CustomerIsBanned(val customer: VatNo) : IllegalArgumentException()

class QuotationNotAccepted : IllegalArgumentException()