package org.infinispan.configuration.cache;

import static org.infinispan.configuration.cache.RecoveryConfiguration.ENABLED;
import static org.infinispan.configuration.cache.RecoveryConfiguration.RECOVERY_INFO_CACHE_NAME;
import static org.infinispan.util.logging.Log.CONFIG;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.transaction.TransactionMode;

/**
 * Defines recovery configuration for the cache.
 *
 * @author pmuir
 *
 */
public class RecoveryConfigurationBuilder extends AbstractTransportConfigurationChildBuilder implements Builder<RecoveryConfiguration> {
   private final AttributeSet attributes;

   RecoveryConfigurationBuilder(TransactionConfigurationBuilder builder) {
      super(builder);
      attributes = RecoveryConfiguration.attributeDefinitionSet();
   }

   @Override
   public AttributeSet attributes() {
      return attributes;
   }

   /**
    * Enable recovery for this cache
    */
   public RecoveryConfigurationBuilder enable() {
      attributes.attribute(ENABLED).set(true);
      return this;
   }

   /**
    * Disable recovery for this cache
    */
   public RecoveryConfigurationBuilder disable() {
      attributes.attribute(ENABLED).set(false);
      return this;
   }

   /**
    * Enable recovery for this cache
    */
   public RecoveryConfigurationBuilder enabled(boolean enabled) {
      attributes.attribute(ENABLED).set(enabled);
      return this;
   }

   boolean isEnabled() {
      return attributes.attribute(ENABLED).get();
   }

   /**
    * Sets the name of the cache where recovery related information is held. If not specified
    * defaults to a cache named {@link RecoveryConfiguration#DEFAULT_RECOVERY_INFO_CACHE}
    */
   public RecoveryConfigurationBuilder recoveryInfoCacheName(String recoveryInfoName) {
      attributes.attribute(RECOVERY_INFO_CACHE_NAME).set(recoveryInfoName);
      return this;
   }

   @Override
   public void validate() {
      if (!attributes.attribute(ENABLED).get()) {
         return; //nothing to validate
      }
      if (transaction().transactionMode() == TransactionMode.NON_TRANSACTIONAL) {
         throw CONFIG.recoveryNotSupportedWithNonTxCache();
      }
      if (transaction().useSynchronization()) {
         throw CONFIG.recoveryNotSupportedWithSynchronization();
      }
   }

   @Override
   public void validate(GlobalConfiguration globalConfig) {
      validate();
   }

   @Override
   public RecoveryConfiguration create() {
      return new RecoveryConfiguration(attributes.protect());
   }

   @Override
   public RecoveryConfigurationBuilder read(RecoveryConfiguration template) {
      this.attributes.read(template.attributes());

      return this;
   }

   @Override
   public String toString() {
      return "RecoveryConfigurationBuilder [attributes=" + attributes + "]";
   }


}
