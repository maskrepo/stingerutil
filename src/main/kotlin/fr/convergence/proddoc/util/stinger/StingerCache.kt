package fr.convergence.proddoc.util.stinger

import fr.convergence.proddoc.model.lib.obj.MaskMessage
import java.io.InputStream
import javax.enterprise.context.ApplicationScoped

/**
 * Gère le cache des callbacks depuis Stinger
 */
@ApplicationScoped
class StingerCache {

    /**
     * Pour chaque clef il y a deux méthodes en cache :
     *  - la première c'est la méthode qui sera appelé lorsque Stinger demandera le stream vers le fichier
     *  - la deuxième c'est la méthode qui sera appelé lorsque Stinger aura fait une réponse sur kafka suite au stockage du fichier
     */
    private var demandeMap: MutableMap<String, Pair<(MaskMessage) -> InputStream, (MaskMessage) -> Unit>> =
        mutableMapOf()

    fun referencerLesMethodesDeCallbacks(
        reference: String,
        callbackSuiteAuPOST: (MaskMessage) -> InputStream,
        callbackSuiteALaReponseDansKafka: (MaskMessage) -> Unit
    ) {
        demandeMap.put(reference, Pair(callbackSuiteAuPOST, callbackSuiteALaReponseDansKafka))
    }

    fun getCallbacks(maskMessage: MaskMessage): Pair<(MaskMessage) -> InputStream, (MaskMessage) -> Unit>? {
        val idReference = maskMessage.entete.idReference
        return demandeMap.get(idReference)
    }
}