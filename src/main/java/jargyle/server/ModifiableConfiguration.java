package jargyle.server;

import java.util.ArrayList;
import java.util.List;

final class ModifiableConfiguration extends Configuration {
	
	private final List<Setting> settings;
	
	public ModifiableConfiguration() {
		this.settings = new ArrayList<Setting>();
	}
	
	public void add(final Configuration configuration) {
		this.addSettings(configuration.getSettings());
	}

	public void addSetting(final Setting sttng) {
		this.settings.add(sttng);
	}
	
	public void addSettings(final Settings sttngs) {
		List<Setting> sttngsList = sttngs.toList();
		if (sttngsList.isEmpty()) {
			return;
		}
		this.settings.addAll(sttngsList);
	}

	@Override
	public Settings getSettings() {
		return Settings.newInstance(this.settings);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [settings=")
			.append(this.settings)
			.append("]");
		return builder.toString();
	}
	
}