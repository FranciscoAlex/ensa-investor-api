package ao.co.ensa.investor.model.enums;

/**
 * Categories for the static_data_definitions table.
 * Each category groups related reference data for frontend use.
 */
public enum StaticDataCategory {

    // Geographic
    PROVINCES("Províncias de Angola"),
    MUNICIPALITIES("Municípios"),
    COUNTRIES("Países"),

    // Document Types
    DOCUMENT_TYPES("Tipos de Documento"),

    // Financial
    CURRENCIES("Moedas"),
    PAYMENT_METHODS("Métodos de Pagamento"),
    INVESTMENT_TYPES("Tipos de Investimento"),
    RISK_PROFILES("Perfis de Risco"),

    // Insurance
    INSURANCE_CATEGORIES("Categorias de Seguro"),
    POLICY_TYPES("Tipos de Apólice"),
    CLAIM_STATUS("Estados de Reclamação"),

    // General
    GENDERS("Géneros"),
    MARITAL_STATUS("Estado Civil"),
    OCCUPATION_TYPES("Tipos de Ocupação"),
    NOTIFICATION_TYPES("Tipos de Notificação"),

    // System
    ACCOUNT_STATUS("Estados de Conta"),
    VERIFICATION_STATUS("Estados de Verificação");

    private final String description;

    StaticDataCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
