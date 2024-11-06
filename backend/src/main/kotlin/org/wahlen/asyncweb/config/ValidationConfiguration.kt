package org.wahlen.asyncweb.config

import jakarta.validation.ConstraintViolationException
import jakarta.validation.MessageInterpolator.Context
import jakarta.validation.Validator
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*

@Configuration
class ValidationConfig {

    @Bean
    fun validator(): LocalValidatorFactoryBean {
        val factoryBean = LocalValidatorFactoryBean()
        factoryBean.messageInterpolator = object : ResourceBundleMessageInterpolator() {
            override fun interpolate(message: String, context: Context, locale: Locale): String {
                return super.interpolate(message, context, Locale.ENGLISH)
            }
            override fun interpolate(message: String, context: Context): String {
                return super.interpolate(message, context, Locale.ENGLISH)
            }
        }
        return factoryBean
    }
}

fun Validator.checkValid(obj: Any) {
    val violations = this.validate(obj)
    if (violations.isNotEmpty()) {
        throw ConstraintViolationException(violations)
    }
}
