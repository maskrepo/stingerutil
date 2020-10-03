package fr.convergence.proddoc.util.stinger

import fr.convergence.proddoc.model.lib.obj.MaskMessage
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.*
import javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR

@Path("/stinger")
@ApplicationScoped
open class StingerControleur(@Inject open val stingerCache: StingerCache) {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(StingerControleur::class.java)
    }

    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_JSON)
    open fun receptionPostStinger(maskMessage: MaskMessage): Response {
        LOG.info("Callback, Stinger vient chercher le stream sur le fichier, la méthode qui va générer ce fichier va être appelée avec ce message : ${maskMessage}")

        return try {
            val callbacks = stingerCache.getCallbacks(maskMessage)
            if (callbacks == null) {
                throw IllegalStateException("Impossible de retrouver cette référence : $maskMessage dans les callbacks de Stinger")
            }

            val metthodeQuiVaGenererLeStreamSurLeFichier = callbacks.first
            ok(metthodeQuiVaGenererLeStreamSurLeFichier(maskMessage))

        } catch (ex: Exception) {
            LOG.error("Erreur lors de l'appel (depuis Stinger) à la méthode qui doit générer le fichier à stocker", ex)
            status(INTERNAL_SERVER_ERROR.statusCode, ex.message)
        }.build()
    }
}