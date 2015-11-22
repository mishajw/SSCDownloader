package com.mishawagner.sscdownloader.comet

import com.mishawagner.sscdownloader.Organiser
import net.liftweb.http.{CometActor, RenderOut}

/**
 * Created by misha on 14/11/15.
 *
 * Handles notifying the user of downloads
 */
class CometNotifier extends CometActor {
  // Register the CometNotifier so it can be contacted
  Organiser registerNotifier this

  override def render: RenderOut =
    "div *" #>
    <table class="table" id="download-status">
      <thead>
        <tr>
          <td>Status</td>
          <td>Location</td>
        </tr>
      </thead>
      <tbody>
        {Organiser.getDownloaders map (downloader => downloader.toHtml)}
      </tbody>
    </table>
}
