package com.example.telephases.di;

import com.example.telephases.network.ApiInterface;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class NetworkModule_ProvideApiInterfaceFactory implements Factory<ApiInterface> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideApiInterfaceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ApiInterface get() {
    return provideApiInterface(retrofitProvider.get());
  }

  public static NetworkModule_ProvideApiInterfaceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideApiInterfaceFactory(retrofitProvider);
  }

  public static ApiInterface provideApiInterface(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideApiInterface(retrofit));
  }
}
