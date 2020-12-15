package com.mydrawn.ARDF.BluetoothExample;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ADGattUUID {

    public static final UUID ClientCharacteristicConfiguration = uuidFromShortString("2902");

    /*
     * A&D Custom
     */
    public static final UUID AndCustomWeightScaleService = UUID.fromString("23434100-1FE4-1EFF-80CB-00FF78297D8B");
    public static final UUID AndCustomWeightScaleMeasurement = UUID.fromString("23434101-1FE4-1EFF-80CB-00FF78297D8B");

    public static final UUID AndCustomService = UUID.fromString("233BF000-5A34-1B6D-975C-000D5690ABE4");
    public static final UUID AndCustomCharacteristic = UUID.fromString("233BF001-5A34-1B6D-975C-000D5690ABE4");
    /*
     * Services
     */
    public static final UUID BloodPressureService = uuidFromShortString("1810");
    public static final UUID CurrentTimeService = uuidFromShortString("1805");
    public static final UUID DeviceInformationService = uuidFromShortString("180a");
    public static final UUID HealthThermometerService = uuidFromShortString("1809");
    public static final UUID WeightScaleService = uuidFromShortString("181d");

    /*
     * Characteristics
     */
    public static final UUID BloodPressureMeasurement = uuidFromShortString("2a35");
    public static final UUID CurrentTime = uuidFromShortString("2a2b");
    public static final UUID DateTime = uuidFromShortString("2a08");
    public static final UUID FirmwareRevisionString = uuidFromShortString("2a26");
    public static final UUID TemperatureMeasurement = uuidFromShortString("2a1c");
    public static final UUID WeightScaleMeasurement = uuidFromShortString("2a9d");
    //ACSG-10
    public static final UUID AndCustomtrackerService = UUID.fromString("11127000-B364-11E4-AB27-0800200C9A66");
    public static final UUID AndCustomtrackerChar1 = UUID.fromString("11127001-B364-11E4-AB27-0800200C9A66");
    public static final UUID AndCustomtrackerChar2 = UUID.fromString("11127002-B364-11E4-AB27-0800200C9A66");
    public static final UUID AndCustomtrackerService2 = UUID.fromString("1A0934F0-B364-11E4-AB27-0800200C9A66");
    public static final UUID AndCustomtrackerService2Char2 = UUID.fromString("1A0934F1-B364-11E4-AB27-0800200C9A66");

    public static List<UUID> ServicesUUIDs = new ArrayList<UUID>();
    public static List<UUID> MeasuCharacUUIDs = new ArrayList<UUID>();

    static {
        ServicesUUIDs.add(AndCustomWeightScaleService);
        ServicesUUIDs.add(BloodPressureService);
        ServicesUUIDs.add(WeightScaleService);
        ServicesUUIDs.add(HealthThermometerService);
        ServicesUUIDs.add(AndCustomtrackerService); //ACSG-10
        ServicesUUIDs.add(AndCustomtrackerService2); //ACSG-10

        MeasuCharacUUIDs.add(AndCustomWeightScaleMeasurement);
        MeasuCharacUUIDs.add(BloodPressureMeasurement);
        MeasuCharacUUIDs.add(WeightScaleMeasurement);
        MeasuCharacUUIDs.add(TemperatureMeasurement);
        MeasuCharacUUIDs.add(AndCustomtrackerChar1); //ACSG-10
        MeasuCharacUUIDs.add(AndCustomtrackerChar2); //ACSG-10
        MeasuCharacUUIDs.add(AndCustomtrackerService2Char2); //ACSG-10
    }


    public static UUID uuidFromShortString(String uuid) {
        return UUID.fromString(String.format("0000%s-0000-1000-8000-00805f9b34fb", uuid));
    }
}
