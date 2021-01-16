package com.orgella.usersmanagement.application.feign

import com.orgella.usersmanagement.application.response.AuctionsResponse
import feign.FeignException
import feign.hystrix.FallbackFactory
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "auctions-ws", fallbackFactory = AuctionsServiceFallback::class)
interface AuctionsServiceClient {

    @GetMapping("/users/{userId}/auctions")
    fun getAuctionsForUser(@PathVariable userId: String): List<AuctionsResponse>
}

@Component
internal class AuctionsFallbackFactory : FallbackFactory<AuctionsServiceClient> {
    override fun create(cause: Throwable): AuctionsServiceClient {
        return AuctionsServiceFallback(cause)
    }
}

internal class AuctionsServiceFallback(
    private val cause: Throwable? = null
) : AuctionsServiceClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun getAuctionsForUser(userId: String): List<AuctionsResponse> {
        if (cause is FeignException
            && cause.status() == 404
        ) {
            logger.error(
                "404 error took place when getAuctions was called with userId: "
                        + userId + ". Error message: "
                        + cause.getLocalizedMessage()
            )
        } else {
            logger.error(
                "Other error took place: " + cause!!.localizedMessage
            )
        }
        return emptyList()
    }
}