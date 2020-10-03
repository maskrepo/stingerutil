package fr.convergence.proddoc.util.stinger

import fr.convergence.proddoc.model.lib.obj.MaskMessage
import fr.convergence.proddoc.model.metier.DemandeStockageFichier
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import java.io.InputStream
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
open class StingerUtil(
    @ConfigProperty(name = "quarkus.http.host") open val host: String,
    @ConfigProperty(name = "quarkus.http.port") open val port: String,
    @Inject open val stingerCache: StingerCache
) {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(StingerUtil::class.java)
    }

    @Inject
    @field: Channel("stocker_fichier_demande")
    private var retourEmitter: Emitter<MaskMessage>? = null

    open fun stockerResultatSurStinger(
        maskMessage: MaskMessage,
        callbackSuiteAuPOST: (MaskMessage) -> InputStream,
        callbackSuiteAMessageReponseDansKafka: (MaskMessage) -> Unit,
        mediaType: String,
        referenceMetier: String
    ) {

        stingerCache.referencerLesMethodesDeCallbacks(
            maskMessage.entete.idReference,
            callbackSuiteAuPOST,
            callbackSuiteAMessageReponseDansKafka
        )

        val urlCallback = "http://$host:$port/stinger"
        val payload = DemandeStockageFichier(urlCallback, maskMessage, mediaType, referenceMetier)
        val question = MaskMessage.question(payload, maskMessage, maskMessage.entete.idReference)
        LOG.info("On poste la demande de stockage de fichier pour Stinger : $question")

        retourEmitter!!.send(question)
    }

}