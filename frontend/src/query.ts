import { QueryClient } from "@tanstack/vue-query";

export const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: Infinity, // Data never becomes stale
            gcTime: Infinity, // Keep data in cache forever
            refetchOnWindowFocus: false, // No refetch when switching tabs
            refetchOnReconnect: false, // No refetch on network reconnect
            refetchOnMount: false, // No refetch on component mount
        },
    },
});

export enum QueryKeys {
    EXRATE_LATEST = "exRate-latest",
    EXRATE_PERIODIC = "exrate-periodic"
}