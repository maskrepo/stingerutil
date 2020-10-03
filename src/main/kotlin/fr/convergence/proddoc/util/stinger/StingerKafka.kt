package fr.convergence.proddoc.util.stinger

import fr.convergence.proddoc.model.lib.obj.MaskMessage
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import org.eclipse.microprofile.reactive.messaging.Incoming
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
open class StingerKafka(@Inject open val stingerCache: StingerCache) {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(StingerKafka::class.java)
    }

    @Incoming("stocker_fichier_reponse")
    open fun traiterEvenementRetourDeStockage(maskMessage: MaskMessage) {
        val callbacks = stingerCache.getCallbacks(maskMessage)
        if (callbacks == null) {
            LOG.debug("Cette réponse de résultat de stockage de fichier ne concerne pas ce module : $maskMessage")
        } else {
            LOG.info("Le fichier a été stocké dans Stinger, réponse de Stinger : $maskMessage")
            val metthodeQuiVaEtreAppeleApresStockageDuFichier = callbacks.second
            metthodeQuiVaEtreAppeleApresStockageDuFichier(maskMessage)
        }
    }
}