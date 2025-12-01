package com.example.telephases.data.local.database

import com.example.telephases.data.local.entities.EntidadSaludEntity

/**
 * Inicializador de datos para entidades de salud del sistema colombiano
 */
object EntidadesSaludInitializer {
    
    /**
     * Obtiene la lista de entidades de salud del sistema colombiano
     */
    fun getEntidadesSaludColombia(): List<EntidadSaludEntity> {
        return listOf(
            // EPS (Entidades Promotoras de Salud)
            EntidadSaludEntity(
                nombreEntidad = "ASMET SALUD EPS S.A.S.",
                nit = "900123456-7",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@asmet.com",
                contactoPrincipalTelefono = "+57-1-234-5678"
            ),
            EntidadSaludEntity(
                nombreEntidad = "EPS FAMISANAR SAS",
                nit = "900234567-8",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@famisanar.com",
                contactoPrincipalTelefono = "+57-1-234-5679"
            ),
            EntidadSaludEntity(
                nombreEntidad = "EPS SANITAS S.A",
                nit = "900345678-9",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@sanitas.com",
                contactoPrincipalTelefono = "+57-1-234-5680"
            ),
            EntidadSaludEntity(
                nombreEntidad = "EPS SURA",
                nit = "900456789-0",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@sura.com",
                contactoPrincipalTelefono = "+57-1-234-5681"
            ),
            EntidadSaludEntity(
                nombreEntidad = "NUEVA EPS S.A",
                nit = "900567890-1",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@nuevaeps.com",
                contactoPrincipalTelefono = "+57-1-234-5682"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SALUD TOTAL",
                nit = "900678901-2",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@saludtotal.com",
                contactoPrincipalTelefono = "+57-1-234-5683"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SAVIA SALUD EPS",
                nit = "900789012-3",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@saviasalud.com",
                contactoPrincipalTelefono = "+57-1-234-5684"
            ),
            EntidadSaludEntity(
                nombreEntidad = "MEDIMAS EPS S.A.S",
                nit = "900890123-4",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@medimas.com",
                contactoPrincipalTelefono = "+57-1-234-5685"
            ),
            EntidadSaludEntity(
                nombreEntidad = "COMPENSAR EPS",
                nit = "900901234-5",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@compensar.com",
                contactoPrincipalTelefono = "+57-1-234-5686"
            ),
            EntidadSaludEntity(
                nombreEntidad = "COOSALUD ENTIDAD PROMOTORA DE SALUD S.A.",
                nit = "901012345-6",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@coosalud.com",
                contactoPrincipalTelefono = "+57-1-234-5687"
            ),
            
            // ARL (Administradoras de Riesgos Laborales)
            EntidadSaludEntity(
                nombreEntidad = "A.R.L. POSITIVA COMPAÑÍA DE SEGUROS S.A.",
                nit = "901123456-7",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@positiva.com",
                contactoPrincipalTelefono = "+57-1-234-5688"
            ),
            EntidadSaludEntity(
                nombreEntidad = "COLMENA ARL",
                nit = "901234567-8",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@colmena.com",
                contactoPrincipalTelefono = "+57-1-234-5689"
            ),
            EntidadSaludEntity(
                nombreEntidad = "LA EQUIDAD SEGUROS ARL",
                nit = "901345678-9",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@equidad.com",
                contactoPrincipalTelefono = "+57-1-234-5690"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SEGUROS BOLIVAR ARL",
                nit = "901456789-0",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@bolivar.com",
                contactoPrincipalTelefono = "+57-1-234-5691"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SEGUROS DE VIDA DEL ESTADO - ARL",
                nit = "901567890-1",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@estado.com",
                contactoPrincipalTelefono = "+57-1-234-5692"
            ),
            
            // SOAT (Seguro Obligatorio de Accidentes de Tránsito)
            EntidadSaludEntity(
                nombreEntidad = "AXA COLPATRIA SEGUROS S.A.",
                nit = "901678901-2",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@axa.com",
                contactoPrincipalTelefono = "+57-1-234-5693"
            ),
            EntidadSaludEntity(
                nombreEntidad = "LIBERTY DE SEGUROS SOAT",
                nit = "901789012-3",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@liberty.com",
                contactoPrincipalTelefono = "+57-1-234-5694"
            ),
            EntidadSaludEntity(
                nombreEntidad = "MUNDIAL DE SEGUROS",
                nit = "901890123-4",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@mundial.com",
                contactoPrincipalTelefono = "+57-1-234-5695"
            ),
            EntidadSaludEntity(
                nombreEntidad = "PREVISORA S.A COMPAÑÍA DE SEGUROS (SOAT)",
                nit = "901901234-5",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@previsora.com",
                contactoPrincipalTelefono = "+57-1-234-5696"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SEGUROS COMERCIALES BOLIVAR (SOAT)",
                nit = "902012345-6",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@bolivarsoat.com",
                contactoPrincipalTelefono = "+57-1-234-5697"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SURAMERICANA S.A (SOAT)",
                nit = "902123456-7",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@suramericana.com",
                contactoPrincipalTelefono = "+57-1-234-5698"
            ),
            
            // Entidades Gubernamentales
            EntidadSaludEntity(
                nombreEntidad = "ADRES",
                nit = "800123456-7",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@adres.gov.co",
                contactoPrincipalTelefono = "+57-1-234-5699"
            ),
            EntidadSaludEntity(
                nombreEntidad = "FONDO NACIONAL DE SALUD DE POBLACION PRIVADA",
                nit = "800234567-8",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@fonsalud.gov.co",
                contactoPrincipalTelefono = "+57-1-234-5700"
            ),
            EntidadSaludEntity(
                nombreEntidad = "DIRECCION GENERAL DE SANIDAD MILITAR",
                nit = "800345678-9",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@sanidadmilitar.gov.co",
                contactoPrincipalTelefono = "+57-1-234-5701"
            ),
            EntidadSaludEntity(
                nombreEntidad = "DIRECCION SECCIONAL DE SALUD DE ANTIOQUIA",
                nit = "800456789-0",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@saludantioquia.gov.co",
                contactoPrincipalTelefono = "+57-1-234-5702"
            ),
            
            // IPS (Instituciones Prestadoras de Salud)
            EntidadSaludEntity(
                nombreEntidad = "DISPENSARIO MÉDICO MEDELLÍN",
                nit = "900567890-2",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@dispensario.com",
                contactoPrincipalTelefono = "+57-4-234-5678"
            ),
            EntidadSaludEntity(
                nombreEntidad = "E.S.E HOSPITAL LA MARIA",
                nit = "900678901-3",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@hospitalmaria.com",
                contactoPrincipalTelefono = "+57-4-234-5679"
            ),
            EntidadSaludEntity(
                nombreEntidad = "IPS SALUD ANTIOQUIA LTDA",
                nit = "900789012-4",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@ipsantioquia.com",
                contactoPrincipalTelefono = "+57-4-234-5680"
            ),
            EntidadSaludEntity(
                nombreEntidad = "MEDISALUD LTDA I.P.S.",
                nit = "900890123-5",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@medisalud.com",
                contactoPrincipalTelefono = "+57-4-234-5681"
            ),
            EntidadSaludEntity(
                nombreEntidad = "FUNDACION MEDICO PREVENTIVA",
                nit = "900901234-6",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@fundacionmedica.com",
                contactoPrincipalTelefono = "+57-1-234-5703"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SUMIMEDICAL S.A.S",
                nit = "901012345-7",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@sumimedical.com",
                contactoPrincipalTelefono = "+57-1-234-5704"
            ),
            
            // Otras Entidades
            EntidadSaludEntity(
                nombreEntidad = "ATENCION A PARTICULARES",
                nit = "901123456-8",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@particulares.com",
                contactoPrincipalTelefono = "+57-1-234-5705"
            ),
            EntidadSaludEntity(
                nombreEntidad = "ASOCIACION MUTUAL SER",
                nit = "901234567-9",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@mutualser.com",
                contactoPrincipalTelefono = "+57-1-234-5706"
            ),
            EntidadSaludEntity(
                nombreEntidad = "ASOCIACION INDIGENA DEL CAUCA (AIC)",
                nit = "901345678-0",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@aic.com",
                contactoPrincipalTelefono = "+57-2-234-5678"
            ),
            EntidadSaludEntity(
                nombreEntidad = "CAJACOPI ATLANTICO - CAJA COMP FAMILIAR",
                nit = "901456789-1",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@cajacopi.com",
                contactoPrincipalTelefono = "+57-5-234-5678"
            ),
            EntidadSaludEntity(
                nombreEntidad = "CAPITALSALUD EPS-S",
                nit = "901567890-2",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@capitalsalud.com",
                contactoPrincipalTelefono = "+57-1-234-5707"
            ),
            EntidadSaludEntity(
                nombreEntidad = "COMFAORIENTE EPS-S",
                nit = "901678901-3",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@comfaoriente.com",
                contactoPrincipalTelefono = "+57-1-234-5708"
            ),
            EntidadSaludEntity(
                nombreEntidad = "ECOOPSOS EPS S.A.S",
                nit = "901789012-4",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@ecoopos.com",
                contactoPrincipalTelefono = "+57-1-234-5709"
            ),
            EntidadSaludEntity(
                nombreEntidad = "ECOPETROL S.A",
                nit = "901890123-5",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@ecopetrol.com",
                contactoPrincipalTelefono = "+57-1-234-5710"
            ),
            EntidadSaludEntity(
                nombreEntidad = "EMPRESA PROMOTORA DE SALUD ECOOPSOS EPS S.A.S",
                nit = "901901234-6",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@ecoopos.com",
                contactoPrincipalTelefono = "+57-1-234-5711"
            ),
            EntidadSaludEntity(
                nombreEntidad = "FIDEICOMISO FONDO NACIONAL DE SALUD",
                nit = "902012345-7",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@fideicomiso.com",
                contactoPrincipalTelefono = "+57-1-234-5712"
            ),
            EntidadSaludEntity(
                nombreEntidad = "FONDO DE PASIVO SOCIAL FERROCARRILES",
                nit = "902123456-7",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@ferrocarriles.com",
                contactoPrincipalTelefono = "+57-1-234-5713"
            ),
            EntidadSaludEntity(
                nombreEntidad = "MAPFRE SEGUROS GENERAL DE COLOMBIA S.A.",
                nit = "902234567-8",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@mapfre.com",
                contactoPrincipalTelefono = "+57-1-234-5714"
            ),
            EntidadSaludEntity(
                nombreEntidad = "REGIONAL DE ASEGURAMIENTO EN SALUD № 5",
                nit = "902345678-9",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@regional5.com",
                contactoPrincipalTelefono = "+57-1-234-5715"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SEGUROS DE VIDA SURAMERICANA S.A",
                nit = "902456789-0",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@vidasuramericana.com",
                contactoPrincipalTelefono = "+57-1-234-5716"
            ),
            EntidadSaludEntity(
                nombreEntidad = "SEGUROS DEL ESTADO S.A",
                nit = "902567890-1",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@segurosestado.com",
                contactoPrincipalTelefono = "+57-1-234-5717"
            ),
            EntidadSaludEntity(
                nombreEntidad = "UNION TEMPORAL RED INTEGRADA FOSCA",
                nit = "902678901-2",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@utfosca.com",
                contactoPrincipalTelefono = "+57-1-234-5718"
            ),
            EntidadSaludEntity(
                nombreEntidad = "UNION TEMPORAL VIHVIR UNIDOS POR LA VIDA",
                nit = "902789012-3",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@vihvivir.com",
                contactoPrincipalTelefono = "+57-1-234-5719"
            ),
            EntidadSaludEntity(
                nombreEntidad = "UT NUEVO FOSYGA",
                nit = "902890123-4",
                contactoPrincipalNombre = "Director General",
                contactoPrincipalEmail = "contacto@utfosyga.com",
                contactoPrincipalTelefono = "+57-1-234-5720"
            )
        )
    }
}
