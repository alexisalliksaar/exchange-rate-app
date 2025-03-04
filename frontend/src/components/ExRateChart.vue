<script setup lang="ts">
import { useLayout } from '@/layout/composables/layout'
import type ExchangeRateDto from '@/model/ExchangeRateDto'

import { dateFromStr } from '@/utils/dateUtils'
import type { ChartConfiguration } from 'chart.js'
import 'chartjs-adapter-date-fns'
import { enGB } from 'date-fns/locale'
import _ from "lodash"
import { onMounted, ref, watch } from 'vue'

const { isDarkTheme } = useLayout()

const props = defineProps<{
    curr: string,
    data: ExchangeRateDto[]
}>()

const defaultConfig: ChartConfiguration<"line", { x: Date, y: number }[]> = {
    type: "line",
    data: {
        datasets: []
    },
    options: {
        plugins: {
            title: {
                text: `${props.curr} vs EUR Exchange Rates`,
                display: true
            },
            legend: {
                display: false
            }
        },
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            x: {
                type: 'time',
                time: {
                    tooltipFormat: "dd.MM.yyyy"
                },
                title: {
                    display: true,
                    text: 'Date'
                },
                adapters: {
                    date: {
                        locale: enGB
                    }
                }
            },
            y: {
                title: {
                    display: true,
                    text: 'Value'
                }
            }
        },
    }
}

const chartConfig = ref<ChartConfiguration<"line", { x: Date, y: number }[]>>(defaultConfig)

watch(() => props.data, (newVal) => {
    updateChartData(newVal)
})


function updateChartData(data: ExchangeRateDto[]) {
    const documentStyle = getComputedStyle(document.documentElement)
    chartConfig.value.data.datasets = [{
        label: props.curr,
        fill: false,
        data: data.map((eR) => { return {y: eR.exRateValue, x: dateFromStr(eR.date) }}),
        backgroundColor: documentStyle.getPropertyValue('--p-primary-500'),
        borderColor: documentStyle.getPropertyValue('--p-primary-500')
    }]
}

function updateChartConfigColors() {
    const documentStyle = getComputedStyle(document.documentElement)
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary')
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border')

    chartConfig.value.data.datasets.forEach((dataset) => {
        dataset.backgroundColor = documentStyle.getPropertyValue('--p-primary-500')
        dataset.borderColor = documentStyle.getPropertyValue('--p-primary-500')
    })
    chartConfig.value.options = _.merge(chartConfig.value.options, 
        {
            scales: {
                x: {
                    ...chartConfig.value.options!.scales!.x,
                    ticks: {
                        color: textColorSecondary
                    },
                    grid: {
                        color: surfaceBorder,
                        drawBorder: false
                    }
                },
                y: {
                    ...chartConfig.value.options!.scales!.y,
                    ticks: {
                        color: textColorSecondary
                    },
                    grid: {
                        color: surfaceBorder,
                        drawBorder: false
                    }
                }
            }
        }
    )
}

watch(
    [isDarkTheme],
    () => { updateChartConfigColors() },
    { immediate: true }
)

onMounted(() => {
    updateChartConfigColors()
    updateChartData(props.data)
})

</script>

<template>
    <Chart type="line" :data="chartConfig.data" :options="chartConfig.options"/>
</template>

