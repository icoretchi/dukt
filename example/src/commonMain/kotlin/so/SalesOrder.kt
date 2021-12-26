package com.corex.erp.domain.so

import com.corex.erp.domain.shared.VatNo

class SalesOrder(private val emit: SalesOrderEmit) : SalesOrderEmit {
    companion object {
        lateinit var salesBans: SalesBans
    }

    lateinit var customer: VatNo

    var delivered = false

    var quotationAccepted = false

    fun doAcceptQuotation() {
        if (!quotationAccepted) emit.onSalesOrderConfirmed()
    }

    fun doRequestForQuotation(customer: VatNo) {
        if (customer in salesBans) throw CustomerIsBanned(customer)
        emit.onQuotationCreated(customer)
    }

    fun doChangeCustomer(customer: VatNo) {
        if (customer == this.customer) return
        if (customer in salesBans) throw CustomerIsBanned(customer)
        emit.onCustomerChanged(customer)
    }

    fun doDeliver() {
        if (quotationAccepted) emit.onDelivered()
        else throw QuotationNotAccepted()
    }

    override fun onCustomerChanged(customer: VatNo) {
        this.customer = customer
        quotationAccepted = false
    }

    override fun onDelivered() {
        delivered = true
    }

    override fun onSalesOrderConfirmed() {
        quotationAccepted = true
    }

    override fun onQuotationCreated(customer: VatNo) {
        this.customer = customer
    }
}