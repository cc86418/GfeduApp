package cn.jun.bean;


public class PackageProduct {
    private int Kid;
    private int ClassId;
    private int ClassTypeId;
    private int BuyType;

    public int getKid() {
        return Kid;
    }

    public void setKid(int kid) {
        Kid = kid;
    }

    public int getClassId() {
        return ClassId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public int getClassTypeId() {
        return ClassTypeId;
    }

    public void setClassTypeId(int classTypeId) {
        ClassTypeId = classTypeId;
    }

    public int getBuyType() {
        return BuyType;
    }

    public void setBuyType(int buyType) {
        BuyType = buyType;
    }

    @Override
    public int hashCode() {
//        return Objects.hash(name, age, passport);
        int result = 17;
//        result = 31 * result + passport.hashCode();
        result = 31 * result + Kid;
        result = 31 * result + ClassId;
        result = 31 * result + ClassTypeId;
        result = 31 * result + BuyType;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PackageProduct)) {
            return false;
        }
        PackageProduct PackageProduct = (PackageProduct) obj;
        return PackageProduct.Kid == Kid &&
                PackageProduct.ClassId == ClassId &&
                PackageProduct.ClassTypeId == ClassTypeId &&
                PackageProduct.BuyType == BuyType;
//        PackageProduct.passport.equals(passport);
    }
}
