import type { AxiosInstance } from "axios";
import axiosInstance from "@/axios"
import type ExchangeRateDto from "@/model/ExchangeRateDto";

export default class ExchangeRateService {
    private readonly axios: AxiosInstance

    constructor() {
        this.axios = axiosInstance
    }

    async fetchLatestExchangeRates(): Promise<ExchangeRateDto[]> {
        return await this.axios.get<ExchangeRateDto[]>("/exchangeRates/latest")
            .then(resp => resp.data)
    }

    async fetchFullCurrencyData(currency: string): Promise<ExchangeRateDto[]> {
        return await this.axios.get<ExchangeRateDto[]>(`/exchangeRates?currency=${currency}`)
            .then(resp => resp.data)
    }
}