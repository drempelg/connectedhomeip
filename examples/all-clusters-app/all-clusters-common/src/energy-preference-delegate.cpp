#include <app/clusters/energy-preference-server/energy-preference-server.h>

using namespace chip;
using namespace chip::app::Clusters::EnergyPreference;
using namespace chip::app::Clusters::EnergyPreference::Structs;

static BalanceStruct::Type gsEnergyBalances[] = {
    { .step = 0, .label = Optional<chip::CharSpan>("Efficient"_span) },
    { .step = 50, .label = Optional<chip::CharSpan>() },
    { .step = 100, .label = Optional<chip::CharSpan>("Comfort"_span) },
};

static BalanceStruct::Type gsPowerBalances[] = {
    { .step = 0, .label = Optional<chip::CharSpan>("1 Minute"_span) },
    { .step = 12, .label = Optional<chip::CharSpan>("5 Minutes"_span) },
    { .step = 24, .label = Optional<chip::CharSpan>("10 Minutes"_span) },
    { .step = 36, .label = Optional<chip::CharSpan>("15 Minutes"_span) },
    { .step = 48, .label = Optional<chip::CharSpan>("20 Minutes"_span) },
    { .step = 60, .label = Optional<chip::CharSpan>("25 Minutes"_span) },
    { .step = 70, .label = Optional<chip::CharSpan>("30 Minutes"_span) },
    { .step = 80, .label = Optional<chip::CharSpan>("60 Minutes"_span) },
    { .step = 90, .label = Optional<chip::CharSpan>("120 Minutes"_span) },
    { .step = 100, .label = Optional<chip::CharSpan>("Never"_span) },
};

// assumes it'll be the only delegate for it's lifetime.
struct EPrefDelegate : public Delegate
{
    EPrefDelegate();
    virtual ~EPrefDelegate();

    CHIP_ERROR GetEnergyBalanceAtIndex(chip::EndpointId aEndpoint, size_t aIndex, BalanceStruct::Type & balance) override;
    CHIP_ERROR GetEnergyPriorityAtIndex(chip::EndpointId aEndpoint, size_t aIndex, EnergyPriorityEnum & priority) override;
    CHIP_ERROR GetLowPowerModeSensitivityAtIndex(chip::EndpointId aEndpoint, size_t aIndex, BalanceStruct::Type & balance) override;

    size_t GetNumEnergyBalances(chip::EndpointId aEndpoint) override;
    size_t GetNumLowPowerModes(chip::EndpointId aEndpoint) override;
};

EPrefDelegate::EPrefDelegate() : Delegate()
{
    VerifyOrDie(GetDelegate() == nullptr);
    SetDelegate(this);
}

EPrefDelegate::~EPrefDelegate()
{
    VerifyOrDie(GetDelegate() == this);
    SetDelegate(nullptr);
}

size_t EPrefDelegate::GetNumEnergyBalances(chip::EndpointId aEndpoint)
{
    return (ArraySize(gsEnergyBalances));
}

size_t EPrefDelegate::GetNumLowPowerModes(chip::EndpointId aEndpoint)
{
    return (ArraySize(gsEnergyBalances));
}

CHIP_ERROR
EPrefDelegate::GetEnergyBalanceAtIndex(chip::EndpointId aEndpoint, size_t aIndex, BalanceStruct::Type & balance)
{
    if (aIndex < GetNumEnergyBalances(aEndpoint))
    {
        balance = gsEnergyBalances[aIndex];
        return CHIP_NO_ERROR;
    }
    return CHIP_ERROR_NOT_FOUND;
}

CHIP_ERROR
EPrefDelegate::GetEnergyPriorityAtIndex(chip::EndpointId aEndpoint, size_t aIndex, EnergyPriorityEnum & priority)
{
    static EnergyPriorityEnum priorities[] = { EnergyPriorityEnum::kEfficiency, EnergyPriorityEnum::kComfort };

    if (aIndex < (sizeof(priorities) / sizeof(priorities[0])))
    {
        priority = priorities[aIndex];
        return CHIP_NO_ERROR;
    }

    return CHIP_ERROR_NOT_FOUND;
}

CHIP_ERROR
EPrefDelegate::GetLowPowerModeSensitivityAtIndex(chip::EndpointId aEndpoint, size_t aIndex, BalanceStruct::Type & balance)
{
    if (aIndex < GetNumLowPowerModes(aEndpoint))
    {
        balance = gsPowerBalances[aIndex];
        return CHIP_NO_ERROR;
    }

    return CHIP_ERROR_NOT_FOUND;
}

static EPrefDelegate gsDelegate;
