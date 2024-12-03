package com.grupo01.spring.model;

/**
 * Enumeración para representar las plataformas de los juegos.
 * 
 *
 * @version 1.0
 * @author Raul
 * @date 29/11/2024
 */
public enum Platform {
    Wii,
    NES,
    GB,
    DS,
    X360,
    PS3,
    PS2,
    SNES,
    GBA,
    PS4,
    PS,
    _3DS,
    _2600,
    GC,
    WiiU,
    XB,
    PC,
    GEN,
    N64,
    XOne,
    PSP,
    PSV,
    DC,
    SAT,
    PCFX,
    NG,
    SCD,
    WS,
    TG16,
    GG,
    _3DO;

    /**
     * Método para mapear los valores del CSV al Enum Platform.
     *
     * @param platformText El texto de la plataforma a mapear.
     * @return El valor del Enum correspondiente.
     * @throws IllegalArgumentException Si el texto no coincide con ningún valor del Enum.
     */
    public static Platform fromString(String platformText) {
        if (platformText == null || platformText.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la plataforma no puede ser nulo o vacío");
        }

        switch (platformText) {
            case "3DS":
                return _3DS;
            case "2600":
                return _2600;
            case "3DO":
                return _3DO;
            default:
                try {
                    return Platform.valueOf(platformText);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "El texto '" + platformText + "' no coincide con ninguna plataforma conocida.", e);
                }
        }
    }
}
