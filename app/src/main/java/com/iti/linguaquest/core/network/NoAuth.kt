package com.iti.linguaquest.core.network

/**
 * Annotation to bypass the AuthInterceptor.
 * Endpoints annotated with this will not have the Authorization header attached.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NoAuth
