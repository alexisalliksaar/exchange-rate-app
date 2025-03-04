<script lang="ts">

enum ChartMode {
    WEEKLY = "WEEKLY",
    MONTHLY = "MONTHLY",
    ALL_TIME = "ALL_TIME"
}

function chartModeString(chartMode: ChartMode): string {
    switch(chartMode) {
        case ChartMode.WEEKLY: return "Week"
        case ChartMode.MONTHLY: return "Month"
        case ChartMode.ALL_TIME: return "All Time"
    }
}
</script>
<script setup lang="ts">
import type ExchangeRateDto from '@/model/ExchangeRateDto'
import ExchangeRateService from '@/service/ExchangeRateService'
import { QueryKeys } from '@/query'
import { computed, ref } from 'vue'
import { useQuery } from '@tanstack/vue-query'
import { dateFromStr } from '@/utils/dateUtils'

const exchangeRateService = new ExchangeRateService()

const selectedCurrency = ref<string>("USD")

const latestQuery = useQuery({ 
    queryKey: [QueryKeys.EXRATE_LATEST],
    queryFn: fetchLatestData,
})

async function fetchLatestData(): Promise<ExchangeRateDto[]> {
    return exchangeRateService.fetchLatestExchangeRates().then((data) => data)
}

function setSelectedCurrency(curr: string) {
    const newSelectedCurr = (latestQuery.data.value ?? [])
        .filter((exRate) => exRate.currency.toUpperCase() === curr.toUpperCase())[0] ?? undefined
    if (newSelectedCurr) {
        selectedCurrency.value = newSelectedCurr.currency
    }
}

const periodQuery = useQuery({
  queryKey: [QueryKeys.EXRATE_PERIODIC, selectedCurrency],
  queryFn: async () => { return exchangeRateService.fetchFullCurrencyData(selectedCurrency.value) }
})

const chartMode = ref<ChartMode>(ChartMode.MONTHLY)

const periodQueryData = computed(() => {
    const data = periodQuery.data?.value ?? []
    let filtered: ExchangeRateDto[]
    switch(chartMode.value) {
        case ChartMode.MONTHLY: {
            filtered = filterLastNDays(data, 31)
            break
        }
        case ChartMode.WEEKLY: {
            filtered = filterLastNDays(data, 7)
            break
        }
        default: {
            filtered = data
            break
        }
    }
    return filtered
})

function filterLastNDays(exRates: ExchangeRateDto[], days: number): ExchangeRateDto[] {
    const today = new Date()
    const cutoffDate = new Date()
    cutoffDate.setDate(today.getDate() - days)

    return exRates.filter(eR => dateFromStr(eR.date) >= cutoffDate)
}

</script>
<template>
    <div v-if="latestQuery.data.value">
        <Fluid class="grid grid-cols-12 gap-8">
            <div class="col-span-12">
                <div class="card">
                    <div class="font-semibold text-xl mb-4">Exchange Rate Chart</div>
                    <div class="flex">
                        <div class="flex align-items-center">
                            <p class="m-0 pr-2 font-semibold">Currency: </p>
                            <Select
                                :modelValue="selectedCurrency"
                                @update:modelValue="setSelectedCurrency"
                                :options="latestQuery.data.value.map((eR) => eR.currency)"
                                filter
                                class ="currency-input"
                            />
                        </div>
                        <div class="flex ml-4">
                            <SelectButton 
                                v-model="chartMode" 
                                :options="[ChartMode.MONTHLY, ChartMode.ALL_TIME]"
                                :optionLabel="(val: ChartMode) => chartModeString(val)"
                            />
                        </div>
                    </div>
                    <ExRateChart v-if="periodQuery.data" :curr="selectedCurrency" :data="periodQueryData"/>
                </div>
            </div>
        </Fluid>
    </div>
</template>
<style scoped>

.currency-input {
    width: 100px;
}

.card > .p-chart {
    min-height: 500px;
}
</style>