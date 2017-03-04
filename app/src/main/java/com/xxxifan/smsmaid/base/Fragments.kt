/*
 * Copyright(c) 2016 xxxifan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xxxifan.smsmaid.base


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.annotation.CheckResult
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import android.view.View

/**
 * Created by xifan on 6/7/16.
 */
class Fragments {

    companion object {

        /**
         * checkout with FRAGMENT_CONTAINER(which is defined in BaseActivity, is R.id.fragment_container
         * it will use BaseFragment.getSimpleName() as tag, or SimpleClassName if fallback.
         */
        @CheckResult
        fun checkout(activity: FragmentActivity, fragment: Fragment): SingleOperator {
            return SingleOperator(activity, fragment)
        }

        /**
         * checkout with specified tag
         */
        @CheckResult
        fun checkout(activity: FragmentActivity, fragment: Fragment, tag: String): SingleOperator {
            return SingleOperator(activity, fragment, tag)
        }

        /**
         * checkout previously fragment by tag
         */
        @CheckResult
        fun checkout(activity: FragmentActivity, tag: String): SingleOperator {
            return SingleOperator(activity, tag)
        }

        /**
         * add multi fragments
         */
        @CheckResult
        fun add(activity: FragmentActivity, vararg fragments: Fragment): MultiOperator {
            return MultiOperator(activity, arrayOf(*fragments))
        }

        /**
         * get current visible fragment on container
         */
        fun getCurrentFragment(activity: FragmentActivity, containerId: Int): Fragment {
            return activity.supportFragmentManager.findFragmentById(containerId)
        }

        fun getFragment(activity: FragmentActivity, tag: String): Fragment {
            return activity.supportFragmentManager.findFragmentByTag(tag)
        }

        fun getFragmentList(activity: FragmentActivity): List<Fragment>? {
            return activity.supportFragmentManager.fragments
        }

        private fun getTag(fragment: Fragment): String {
            return if (Strings.isEmpty(fragment.tag))
//            if (fragment is BaseFragment)
//                (fragment as BaseFragment).getSimpleName()
//            else
                fragment.javaClass.name
            else
                fragment.tag
        }
    }

    class SingleOperator {
        private var activity: FragmentActivity? = null
        private var fragment: Fragment? = null
        private var tag: String
        private var transaction: FragmentTransaction?

        private var addToBackStack: Boolean = false
        private var fade: Boolean = false
        private var removeLast: Boolean = false
        private var hideLast = true

        constructor(activity: FragmentActivity, fragment: Fragment) : this(activity, fragment, getTag(fragment))

        @SuppressLint("CommitTransaction")
        constructor(activity: FragmentActivity, fragment: Fragment, tag: String) {
            this.activity = activity
            this.fragment = fragment
            this.tag = tag
            this.transaction = activity.supportFragmentManager.beginTransaction()
        }

        @SuppressLint("CommitTransaction")
        constructor(activity: FragmentActivity, tag: String) {
            this.activity = activity
            this.tag = tag
            this.transaction = activity.supportFragmentManager.beginTransaction()

            // retrieve correct fragment
            val fragments = getFragmentList(activity)
            fragments!!.filter { Strings.equals(it.tag, tag) }
                    .map { this.fragment = it }
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> bindPresenter(presenter: BasePresenter<T>): SingleOperator {
            presenter.setView(fragment as T)
            return this
        }

        /**
         * setArguments to target fragment.
         */
        fun data(data: Bundle): SingleOperator {
            fragment?.arguments = data
            if (fragment == null) {
                print("fragment is null, will not add data to arguments")
            }
            return this
        }

        /**
         * simple string bundle as argument
         */
        fun data(key: String, value: String?): SingleOperator {
            if (fragment != null) {
                val bundle = Bundle()
                bundle.putString(key, value)
                fragment!!.arguments = bundle
            } else {
                print("fragment is null, will not add data to arguments")
            }
            return this
        }

        fun addSharedElement(sharedElement: View, name: String): SingleOperator {
            transaction!!.addSharedElement(sharedElement, name)
            return this
        }

        fun setCustomAnimator(@AnimRes enter: Int, @AnimRes exit: Int): SingleOperator {
            transaction!!.setCustomAnimations(enter, exit)
            return this
        }

        fun setCustomAnimator(@AnimRes enter: Int, @AnimRes exit: Int, @AnimRes popEnter: Int, @AnimRes popExit: Int): SingleOperator {
            transaction!!.setCustomAnimations(enter, exit, popEnter, popExit)
            return this
        }

        fun addToBackStack(add: Boolean): SingleOperator {
            this.addToBackStack = add
            return this
        }

        /**
         * display fade transition
         */
        fun fade(): SingleOperator {
            this.fade = true
            return this
        }

        /**
         * hideLast last fragment, default is true.
         * if you want last to remove, see [.removeLast]
         */
        fun hideLast(hideLast: Boolean): SingleOperator {
            this.hideLast = hideLast
            return this
        }

        /**
         * remove last fragment while checkout.
         */
        fun removeLast(remove: Boolean): SingleOperator {
            this.removeLast = remove
            return this
        }

        fun into(@IdRes containerId: Int) {
            if (fragment == null) {
                print("fragment is null, will do nothing")
                commit()
                return
            }

            // hide or remove last fragment
            if (hideLast || removeLast) {
                val fragments = getFragmentList(activity!!)
                fragments?.filter { it.id == containerId }
                        ?.forEach {
                            if (Strings.equals(it.tag, tag)) {
                                fragment = it
                            } else if (it.isVisible) {
                                it.userVisibleHint = false
                                transaction!!.hide(it)
                                if (removeLast) {
                                    print("last fragment has been totally removed")
                                    transaction!!.remove(it)
                                }
                            }
                        }
            }

            val canAddBackStack = transaction!!.isAddToBackStackAllowed && !transaction!!.isEmpty

            if (fade) {
                transaction!!.setTransition(TRANSIT_FRAGMENT_FADE)
            }

            if (addToBackStack) {
                if (canAddBackStack) {
                    transaction!!.addToBackStack(tag)
                } else {
                    println("addToBackStack called, but this is not permitted")
                }
            }

            if (!fragment!!.isAdded) {
                transaction!!.add(containerId, fragment, tag)
            }

            transaction!!.show(fragment)

            commit()
        }

        private fun commit() {
            transaction!!.commitAllowingStateLoss()

//            if (fragment != null && fragment is BaseFragment) {
//                (fragment as BaseFragment).onVisible()
//            }

            transaction = null
            fragment = null
            activity = null
        }

    }

    // TODO: 6/10/16 MultiOperator is not used that much, so I only give it basic into function here.
    class MultiOperator(var activity: FragmentActivity?, val fragments: Array<Fragment>) {

        fun into(vararg ids: Int) {
            if (ids.size != fragments.size) {
                throw IllegalArgumentException("The length of ids and fragments is not equal.")
            }

            val transaction = activity!!
                    .supportFragmentManager
                    .beginTransaction()
            var tag: String
            var i = 0
            val s = ids.size
            while (i < s) {
                tag = getTag(fragments[i])
                val fragment = fragments[i]
                transaction.replace(ids[i], fragment, tag)
                i++
            }
            transaction.commitAllowingStateLoss()
            activity = null
        }
    }

}
