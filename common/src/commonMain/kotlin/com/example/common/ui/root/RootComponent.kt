package com.example.common.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.example.common.ui.premium_adv.AnotherTestComponent
import com.example.common.ui.premium_adv.IPremiumAdvComponent
import com.example.common.ui.main_dashboard.MainDashboardComponent

interface IRootComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onCountersTabClicked()
    fun onAnotherTestTabClicked()
    // methods will can add

    sealed class Child {
        class MainDashboardChild(val component: MainDashboardComponent) : Child()
        class PremiumAdvChild(val component: IPremiumAdvComponent) : Child()
    }
}

class RootComponent(
    componentContext: ComponentContext
) : IRootComponent, ComponentContext by componentContext//, KoinComponent
{

    private val navigation = StackNavigation<Config>()

    private val _childStack =
        childStack(
            source = navigation,
            initialConfiguration = Config.PremiumAdv,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    override val childStack: Value<ChildStack<*, IRootComponent.Child>> = _childStack

    override fun onCountersTabClicked() {
        //navigation.push(Config.List)
        navigation.bringToFront(Config.MainDashboard)
    }

    override fun onAnotherTestTabClicked() {
        //navigation.push(Config.AnotherTest)
        navigation.bringToFront(Config.PremiumAdv)
    }

    private fun createChild(config: Config, componentContext: ComponentContext): IRootComponent.Child =
        when (config) {
            is Config.MainDashboard -> IRootComponent.Child.MainDashboardChild(
                createItemList(
                    componentContext
                )
            )
            is Config.PremiumAdv -> IRootComponent.Child.PremiumAdvChild(
                createAnotherTest(
                    componentContext
                )
            )

            else -> {
                IRootComponent.Child.MainDashboardChild(
                    createItemList(
                        componentContext
                    )
                )
            }
        }

    private fun createItemList(componentContext: ComponentContext): MainDashboardComponent =
        MainDashboardComponent(
            componentContext = componentContext,
            onItemSelected = {
                navigation.bringToFront(Config.PremiumAdv)
            }
        ) {
            navigation.bringToFront(Config.PremiumAdv)
        }

//    private fun createItemDetails(componentContext: ComponentContext, config: Config.Functionality): IFunctionalityComponent =
//        FunctionalityComponent(
//            componentContext = componentContext,
//            itemId = config.itemId,
//            onFinished = { navigation.pop() }
//        )

    private fun createAnotherTest(componentContext: ComponentContext): IPremiumAdvComponent =
        AnotherTestComponent(
            componentContext = componentContext,
            onBack = {
                navigation.pop()
            }
        ) {
            navigation.bringToFront(Config.MainDashboard)
        }


    private sealed class Config : Parcelable {
        @Parcelize
        object MainDashboard : Config()

        @Parcelize
        object PremiumAdv : Config()

        @Parcelize
        data class Functionality(val itemId: Long) : Config()
    }
}
