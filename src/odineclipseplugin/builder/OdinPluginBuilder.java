package odineclipseplugin.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import odineclipseplugin.Activator;
import odineclipseplugin.preferences.OdinPreferencePage;

public class OdinPluginBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "odin-eclipse-plugin.sampleOdinPluginBuilder";
	private static final String MARKER_TYPE = "odin-eclipse-plugin.xmlProblem";

	// Odin error format: path/file.odin(line:col) message
	private static final Pattern ERROR_PATTERN =
			Pattern.compile("^(.+\\.odin)\\((\\d+):(\\d+)\\)\\s+(Error|Warning):\\s+(.+)$");

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		IResourceDelta delta = getDelta(getProject());
		if (kind == FULL_BUILD || delta == null) {
			runOdinBuild(monitor);
		} else {
			boolean[] hasOdinChanges = {false};
			try {
				delta.accept(d -> {
					if (d.getResource() instanceof IFile && d.getResource().getName().endsWith(".odin"))
						hasOdinChanges[0] = true;
					return true;
				});
			} catch (CoreException e) { /* ignore */ }
			if (hasOdinChanges[0]) runOdinBuild(monitor);
		}
		return null;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		getProject().deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	private void runOdinBuild(IProgressMonitor monitor) throws CoreException {
		String odinPath = Activator.getDefault().getPreferenceStore().getString(OdinPreferencePage.PREF_ODIN_PATH);
		if (odinPath == null || odinPath.isBlank()) return;

		getProject().deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);

		String projectPath = getProject().getLocation().toOSString();
		try {
			Process process = new ProcessBuilder(odinPath, "build", ".", "-no-entry-point")
					.directory(new File(projectPath))
					.redirectErrorStream(true)
					.start();

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					parseAndMark(line, projectPath);
				}
			}
			process.waitFor();
		} catch (Exception e) {
			// odin not found or failed to launch — silently skip
		}
	}

	private void parseAndMark(String line, String projectPath) {
		Matcher m = ERROR_PATTERN.matcher(line.trim());
		if (!m.matches()) return;

		String filePath = m.group(1).replace('/', File.separatorChar);
		int lineNum = Integer.parseInt(m.group(2));
		String level = m.group(4);
		String message = m.group(5);
		int severity = "Warning".equals(level) ? IMarker.SEVERITY_WARNING : IMarker.SEVERITY_ERROR;

		// Resolve relative path against project
		File f = new File(filePath);
		if (!f.isAbsolute()) f = new File(projectPath, filePath);

		IFile iFile = getProject().getWorkspace().getRoot()
				.getFileForLocation(IPath.fromOSString(f.getAbsolutePath()));
		if (iFile == null || !iFile.exists()) return;

		try {
			IMarker marker = iFile.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			marker.setAttribute(IMarker.LINE_NUMBER, lineNum);
		} catch (CoreException e) { /* ignore */ }
	}
}
