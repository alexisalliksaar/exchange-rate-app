<script setup lang="ts">

import type ExchangeRateDto from '@/model/ExchangeRateDto'
import { QueryKeys } from '@/query'
import ExchangeRateService from '@/service/ExchangeRateService'
import { formatDate } from '@/utils/dateUtils'
import { useQuery } from '@tanstack/vue-query'
import { onMounted, ref, watch } from 'vue'

const selectedCurrencyRate = ref<ExchangeRateDto | null>(null)
const defaultCurr: string = "USD"

const exchangeRateService = new ExchangeRateService()
const { isPending, isError, data } = useQuery({ 
    queryKey: [QueryKeys.EXRATE_LATEST],
    queryFn: fetchLatestData,
})

const customCurrValue = ref<number>(0)
const euroCurrValue = ref<number>(0)


async function fetchLatestData(): Promise<ExchangeRateDto[]> {
    return exchangeRateService.fetchLatestExchangeRates().then((fetchedData) => {
        setSelectedCurrency(defaultCurr, fetchedData)
        return fetchedData
    })
}

function setSelectedCurrency(curr: string, fetchedData?: ExchangeRateDto[]) {

    const newSelectedRate = (fetchedData ?? (data.value ?? []))
        .filter((exRate) => exRate.currency.toUpperCase() === curr.toUpperCase())[0] ?? undefined
    if (newSelectedRate) {
        selectedCurrencyRate.value = newSelectedRate
    }
}

function onCustomCurrExRateChange(newVal: string | undefined) {
    if (newVal) {
        setSelectedCurrency(newVal)
        updateEuroModelValue()
    }
}

function updateEuroModelValue() {
    if (selectedCurrencyRate.value) {
        const customValue = customCurrValue.value
        const customExchangeRate = selectedCurrencyRate.value.exRateValue
        const euroValue = customValue / customExchangeRate
        euroCurrValue.value = euroValue
    }
}

function updateCustomCurrModelValue() {
    if (selectedCurrencyRate.value) {
        const euroValue = euroCurrValue.value
        const customExchangeRate = selectedCurrencyRate.value.exRateValue
        const customValue = euroValue * customExchangeRate
        customCurrValue.value = customValue
    }
}

function onCustomValueUpdate(newVal: number) {
    customCurrValue.value = newVal
    updateEuroModelValue()
}

function onEuroValueUpdate(newVal: number) {
    euroCurrValue.value = newVal
    updateCustomCurrModelValue()
}

watch(() => data.value, () => {
    if (selectedCurrencyRate.value === null) {
        setSelectedCurrency(defaultCurr)
    }
})

onMounted(() => {
    setSelectedCurrency(defaultCurr)
})
</script>
<template>
    <Card>
        <template #title>Exchange Rate Calculator</template>
        <template #content>
            <div v-if="!isPending && data">
                <div class="flex flex-row flex-wrap justify-content-between">
                    <div class="flex flex-grow mt-2 mr-2">
                        <CalculatorInput 
                            :exRate="selectedCurrencyRate?.currency ?? undefined"
                            :exRateOptions="data.map((eR) => eR.currency)"
                            :modelValue="customCurrValue"
                            @updateExRate="onCustomCurrExRateChange"
                            @update:modelValue="onCustomValueUpdate"
                        />
                    </div>
                    <div class="flex flex-grow mt-2">
                        <CalculatorInput 
                            exRate="EUR"
                            :modelValue="euroCurrValue"
                            @update:modelValue="onEuroValueUpdate"
                            :selectable="false"
                        />
                    </div>
                </div>
                <div class="mt-2">
                    <p v-if="selectedCurrencyRate"> Exchange Rate From: {{ formatDate(selectedCurrencyRate.date) }}</p>
                </div>
            </div>
            <div v-else-if="isError">
                <p>Error Fetching data</p>
            </div>
            <div v-else>
            </div>
        </template>
    </Card>
</template>