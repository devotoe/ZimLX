package org.zimmob.zimlx.preferences

import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.R
import com.android.launcher3.compat.LauncherAppsCompat
import com.android.launcher3.compat.UserManagerCompat
import org.zimmob.zimlx.HiddenAppsAdapter

class HiddenAppsFragment : Fragment(), HiddenAppsAdapter.Callback {

    private lateinit var installedApps: List<LauncherActivityInfo>
    private lateinit var adapter: HiddenAppsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return RecyclerView(container!!.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        val recyclerView = view as RecyclerView
        installedApps = getAppsList(context).apply { sortBy { it.label.toString().toLowerCase() } }
        adapter = HiddenAppsAdapter(view.context, installedApps, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    override fun setTitle(newTitle: String) {
        activity!!.title = newTitle
    }

    private fun getAppsList(context: Context): ArrayList<LauncherActivityInfo> {
        val apps = ArrayList<LauncherActivityInfo>()
        val profiles = UserManagerCompat.getInstance(context).userProfiles
        val launcherAppsCompat = LauncherAppsCompat.getInstance(context)
        profiles.forEach {
            apps += launcherAppsCompat.getActivityList(null, it)
        }
        return apps
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.menu_hide_apps, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reset -> {
                activity!!.title = adapter.clearSelection()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}