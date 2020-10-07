package fr.convergence.proddoc.util.stinger

import fr.convergence.proddoc.model.lib.obj.MaskMessage
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.LoggerFactory.getLogger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
open class StingerKafka(@Inject open val stingerCache: StingerCache) {

    companion object {
        private val LOG = getLogger(StingerKafka::class.java)
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