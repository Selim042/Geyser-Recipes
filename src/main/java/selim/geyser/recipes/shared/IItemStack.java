package selim.geyser.recipes.shared;

public interface IItemStack {

	public String getItemNamespace();

	public String getItemKey();

	public int getCount();

	public int getMetadata();

	public Object getNbt();

}
