package com.corex.erp.domain.so

import com.corex.erp.domain.shared.VatNo

interface SalesOrderEmit {
    fun onCustomerChanged(customer: VatNo)

    fun onDelivered()

    fun onSalesOrderConfirmed()

    fun onQuotationCreated(customer: VatNo)
}